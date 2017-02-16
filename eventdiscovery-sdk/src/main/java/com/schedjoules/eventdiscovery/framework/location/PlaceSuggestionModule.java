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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskResult;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.ListItemSelectionAction;
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.ClearAll;
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.ReplaceAll;
import com.schedjoules.eventdiscovery.framework.location.listitems.PlaceSuggestionItem;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.NamedPlace;
import com.schedjoules.eventdiscovery.framework.location.tasks.PlaceByIdTask;
import com.schedjoules.eventdiscovery.framework.location.tasks.PlaceSuggestionQueryTask;
import com.schedjoules.eventdiscovery.framework.searchlist.ResultUpdateListener;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchResultUpdate;

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
public final class PlaceSuggestionModule implements SearchModule, PlaceSuggestionItem.OnClickListener
{
    public static SearchModuleFactory<GeoPlace> FACTORY = new SearchModuleFactory<GeoPlace>()
    {
        @Override
        public SearchModule create(Activity activity, ResultUpdateListener<ListItem> updateListener, ListItemSelectionAction<GeoPlace> itemSelectionAction)
        {
            GoogleApiClient googleApiClient = new GoogleApiClient
                    .Builder(activity)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            /**
             * TODO enableAutomanage OR manual error handling
             * enableAutoManage() on the builder would enable automatic default error handling as well,
             * but it's tricky to get initialization correctly with Activity and retained Fragment lifecycles.
             * Either enable automanage or add 'manual' error handling with addOnConnectionFailedListener().
             *
             * See https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient.Builder.html#enableAutoManage(android.support.v4.app.FragmentActivity, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener)
             */

            googleApiClient.connect();
            return new PlaceSuggestionModule(updateListener, itemSelectionAction, googleApiClient,
                    activity.getString(R.string.schedjoules_location_picker_place_suggestions_title));
        }

    };

    private static final String TAG = "PlaceSuggestionModule";

    private final ResultUpdateListener<ListItem> mUpdateListener;
    private final ListItemSelectionAction<GeoPlace> mSelectionAction;
    private final GoogleApiClient mGoogleApiClient;
    private final CharSequence mHeaderTitle;

    private final ExecutorService mExecutorService;
    private final PlaceSuggestionQueryTask.Client mSuggestionQueryTaskClient;
    private final PlaceByIdTaskClient mPlaceByIdTaskClient;
    private final LinkedBlockingQueue<Runnable> mJobQueue;


    private PlaceSuggestionModule(ResultUpdateListener<ListItem> updateListener,
                                  ListItemSelectionAction<GeoPlace> itemSelectionAction,
                                  GoogleApiClient googleApiClient,
                                  CharSequence headerTitle)
    {
        mUpdateListener = updateListener;
        mSelectionAction = itemSelectionAction;
        mGoogleApiClient = googleApiClient;
        mHeaderTitle = headerTitle;

        mJobQueue = new LinkedBlockingQueue<>();
        mExecutorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, mJobQueue);

        mSuggestionQueryTaskClient = new PlaceSuggestionQueryTaskClient();
        mPlaceByIdTaskClient = new PlaceByIdTaskClient();
    }


    @Override
    public void shutDown()
    {
        mGoogleApiClient.disconnect();
        mExecutorService.shutdown();
    }


    @Override
    public void onPlaceSuggestionSelected(NamedPlace namedPlace)
    {
        new PlaceByIdTask(namedPlace, mPlaceByIdTaskClient).executeOnExecutor(mExecutorService, mGoogleApiClient);
    }


    @Override
    public void onSearchQueryChange(String newQuery)
    {
        mJobQueue.clear();
        new PlaceSuggestionQueryTask(newQuery, mSuggestionQueryTaskClient).executeOnExecutor(mExecutorService, mGoogleApiClient);
    }


    private void onSuggestionsReceived(List<ListItem> newSuggestions, String query)
    {
        if (newSuggestions.isEmpty())
        {
            mUpdateListener.onUpdate(new SearchResultUpdate<>(new ClearAll<ListItem>(), query));
            return;
        }

        for (ListItem newItem : newSuggestions)
        {
            if (newItem instanceof PlaceSuggestionItem)
            {
                ((PlaceSuggestionItem) newItem).setListener(PlaceSuggestionModule.this);
            }
        }

        // TODO Uncomment when there is another module already:
//        if (!newSuggestions.isEmpty())
//        {
//            newSuggestions.add(0, new TextHeaderItem(mHeaderTitle));
//        }

        mUpdateListener.onUpdate(new SearchResultUpdate<>(new ReplaceAll<>(newSuggestions), query));
    }


    private final class PlaceSuggestionQueryTaskClient implements PlaceSuggestionQueryTask.Client
    {

        @Override
        public void onTaskFinish(SafeAsyncTaskResult<List<ListItem>> taskResult, String query)
        {
            try
            {
                onSuggestionsReceived(taskResult.value(), query);
            }
            catch (Exception e)
            {
                // TODO Error handling on UI?
                Log.e(TAG, "Error returned by places suggestion query task.", e);
            }
        }

    }


    private final class PlaceByIdTaskClient implements PlaceByIdTask.Client
    {

        @Override
        public void onTaskFinish(SafeAsyncTaskResult<GeoPlace> result, NamedPlace namedPlace)
        {
            try
            {
                mSelectionAction.onItemSelected(result.value());
            }
            catch (Exception e)
            {
                // TODO Error handling on UI?
                Log.e(TAG, "Error returned by place by id task.", e);
            }
        }
    }
}
