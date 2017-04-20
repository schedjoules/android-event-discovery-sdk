/*
 * Copyright 2017 SchedJoules
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.schedjoules.eventdiscovery.framework.location;

import android.app.Activity;
import android.util.Log;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApis;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.AbstractGoogleApiRequestException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiAutoManagedIssueException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiExecutionException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiNonRecoverableException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiRecoverableException;
import com.schedjoules.eventdiscovery.framework.googleapis.requests.GetAutoCompletePredictionsRequest;
import com.schedjoules.eventdiscovery.framework.googleapis.requests.GetPlaceByIdRequest;
import com.schedjoules.eventdiscovery.framework.googleapis.requests.GoogleApiTask;
import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.ClearAll;
import com.schedjoules.eventdiscovery.framework.list.smart.Clickable;
import com.schedjoules.eventdiscovery.framework.location.listitems.ActionMessageItem;
import com.schedjoules.eventdiscovery.framework.location.listitems.MessageItem;
import com.schedjoules.eventdiscovery.framework.location.listitems.PlaceSuggestionItem;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.CommaSeparated;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.Equalable;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.NamedPlace;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.Clear;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ForcedShowSingle;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ReplaceAll;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.SearchResultUpdate;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ShowSingle;
import com.schedjoules.eventdiscovery.framework.utils.ActivityReloadAction;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * {@link SearchModule} for showing the Google Place API places suggestions.
 *
 * @author Gabor Keszthelyi
 */
public final class PlaceSuggestionModule implements SearchModule
{

    private static final String TAG = "PlaceSuggestionModule";

    private final Activity mActivity;
    private final ResultUpdateListener<ListItem> mUpdateListener;
    private final ItemChosenAction<GeoPlace> mItemChosenAction;
    private final GoogleApis mGoogleApis;
    private final ExecutorService mExecutorService;
    private final LinkedBlockingQueue<Runnable> mJobQueue;


    public PlaceSuggestionModule(Activity activity,
                                 ResultUpdateListener<ListItem> updateListener,
                                 ItemChosenAction<GeoPlace> itemChosenAction,
                                 GoogleApis googleApis)
    {
        mActivity = activity;
        mUpdateListener = updateListener;
        mItemChosenAction = itemChosenAction;
        mGoogleApis = googleApis;
        mJobQueue = new LinkedBlockingQueue<>();
        mExecutorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, mJobQueue);
    }


    @Override
    public void shutDown()
    {
        mJobQueue.clear();
        mExecutorService.shutdown();
    }


    @Override
    public void onSearchQueryChange(final String newQuery)
    {
        mJobQueue.clear();
        if (newQuery.isEmpty())
        {
            mUpdateListener.onUpdate(new Clear<ListItem>(newQuery));
        }
        else
        {
            executeSuggestionQuery(newQuery);
        }
    }


    private void executeSuggestionQuery(final String query)
    {
        new GoogleApiTask<>(new GetAutoCompletePredictionsRequest(query), new GoogleApiTask.Callback<List<NamedPlace>>()
        {
            @Override
            public void onTaskFinish(GoogleApiTask.Result<List<NamedPlace>> result)
            {
                try
                {
                    onSuggestionsReceived(result.value(), query);
                }
                catch (GoogleApiAutoManagedIssueException e)
                {
                    Log.e(TAG, "Failed to get suggestions", e);
                    // Do nothing, being auto-managed, module will be triggered in onResume()
                }
                catch (GoogleApiNonRecoverableException e)
                {
                    Log.e(TAG, "Failed to get suggestions", e);
                    ListItem errorItem = new MessageItem(mActivity.getText(R.string.schedjoules_location_picker_googleapi_error_unrecovarable));
                    mUpdateListener.onUpdate(new ForcedShowSingle<>(errorItem));
                }
                catch (GoogleApiRecoverableException e)
                {
                    Log.e(TAG, "Failed to get suggestions", e);
                    ListItem errorItem = new ActionMessageItem(
                            mActivity.getText(R.string.schedjoules_location_picker_googleapi_error_recovarable),
                            mActivity.getText(R.string.schedjoules_retry),
                            new ActivityReloadAction(mActivity));
                    mUpdateListener.onUpdate(new ShowSingle<>(errorItem, query));
                }
                catch (GoogleApiExecutionException e)
                {
                    Log.e(TAG, "Failed to get suggestions", e);
                    ListItem errorItem = new ActionMessageItem(
                            mActivity.getString(R.string.schedjoules_location_picker_suggestion_error),
                            mActivity.getString(R.string.schedjoules_retry),
                            new OnClickAction()
                            {
                                @Override
                                public void onClick()
                                {
                                    executeSuggestionQuery(query);
                                }
                            }
                    );
                    mUpdateListener.onUpdate(new ShowSingle<>(errorItem, query));
                }
                catch (AbstractGoogleApiRequestException e)
                {
                    throw new RuntimeException("Unhandled AbstractGoogleApiRequestException");
                }
            }
        }).executeOnExecutor(mExecutorService, mGoogleApis);
    }


    private void onSuggestionsReceived(List<NamedPlace> newSuggestions, String query)
    {
        if (newSuggestions.isEmpty())
        {
            mUpdateListener.onUpdate(new SearchResultUpdate<>(new ClearAll<ListItem>(), query));
            return;
        }

        List<ListItem> newItems = new ArrayList<>();
        for (final NamedPlace namedPlace : newSuggestions)
        {
            ListItem item = new Clickable<>(
                    new PlaceSuggestionItem<>(new Equalable(namedPlace)),
                    new PlaceLookUpAction(namedPlace)
            );
            newItems.add(item);
        }
        mUpdateListener.onUpdate(new ReplaceAll<>(newItems, query));
    }


    private final class PlaceLookUpAction implements OnClickAction
    {

        private final NamedPlace mNamedPlace;


        PlaceLookUpAction(NamedPlace namedPlace)
        {
            mNamedPlace = namedPlace;
        }


        @Override
        public void onClick()
        {
            new GoogleApiTask<>(new GetPlaceByIdRequest(mNamedPlace), new GoogleApiTask.Callback<GeoPlace>()
            {
                @Override
                public void onTaskFinish(GoogleApiTask.Result<GeoPlace> result)
                {
                    try
                    {
                        mItemChosenAction.onItemChosen(result.value());
                    }
                    catch (Exception e)
                    {
                        ListItem errorItem = new ActionMessageItem(
                                mActivity.getString(R.string.schedjoules_location_picker_suggestion_placelookup_error, new CommaSeparated(mNamedPlace)),
                                mActivity.getString(R.string.schedjoules_retry),
                                new PlaceLookUpAction(mNamedPlace)
                        );
                        mUpdateListener.onUpdate(new ForcedShowSingle<>(errorItem));
                        Log.e(TAG, "Failed to get id for place", e);
                    }

                }
            }).executeOnExecutor(mExecutorService, mGoogleApis);
        }

    }
}
