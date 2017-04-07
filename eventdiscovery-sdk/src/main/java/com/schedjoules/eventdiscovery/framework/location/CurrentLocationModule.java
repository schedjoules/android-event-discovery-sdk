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
import android.location.Geocoder;
import android.util.Log;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskResult;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApis;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.AbstractGoogleApiRequestException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiAutoManagedIssueException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiExecutionException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiNonRecoverableException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiRecoverableException;
import com.schedjoules.eventdiscovery.framework.googleapis.requests.GetLastLocationRequest;
import com.schedjoules.eventdiscovery.framework.googleapis.requests.GoogleApiTask;
import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.smart.Clickable;
import com.schedjoules.eventdiscovery.framework.location.listitems.ButtonedMessageItem;
import com.schedjoules.eventdiscovery.framework.location.listitems.MessageItem;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.tasks.GetCityTask;
import com.schedjoules.eventdiscovery.framework.model.ParcelableGeoLocation;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.Clear;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ForcedClear;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ShowSingle;
import com.schedjoules.eventdiscovery.framework.utils.ActivityReloadAction;
import com.schedjoules.eventdiscovery.framework.utils.cache.Cache;
import com.schedjoules.eventdiscovery.framework.utils.cache.TimedSingleValueCache;
import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * {@link SearchModule} for showing the current location (city, country).
 *
 * @author Gabor Keszthelyi
 */
public final class CurrentLocationModule implements SearchModule
{

    private static final int CACHE_CITY_MINUTES = 3;

    private static final String TAG = "CurrentLocationModule";

    private final Activity mActivity;
    private final ResultUpdateListener<ListItem> mUpdateListener;
    private final ItemChosenAction<GeoPlace> mItemChosenAction;
    private final Lazy<Geocoder> mGeocoderFactory;
    private final GoogleApis mGoogleApis;
    private final ExecutorService mExecutorService;
    private final Cache<ParcelableGeoLocation, GeoPlace> mCurrentCityCache;


    public CurrentLocationModule(Activity activity,
                                 ResultUpdateListener<ListItem> updateListener,
                                 ItemChosenAction<GeoPlace> itemChosenAction,
                                 Lazy<Geocoder> geocoderFactory,
                                 GoogleApis googleApis)
    {
        mActivity = activity;
        mUpdateListener = updateListener;
        mItemChosenAction = itemChosenAction;
        mGeocoderFactory = geocoderFactory;
        mGoogleApis = googleApis;

        mExecutorService = Executors.newSingleThreadExecutor();

        mCurrentCityCache = new TimedSingleValueCache<>(CACHE_CITY_MINUTES, TimeUnit.MINUTES);
    }


    @Override
    public void shutDown()
    {
        mExecutorService.shutdown();
    }


    @Override
    public void onSearchQueryChange(String newQuery)
    {
        if (newQuery.isEmpty())
        {
            cacheCurrentLocation();

            ListItem item = new Clickable<>(
                    new MessageItem(mActivity.getString(R.string.schedjoules_location_picker_current_location)),
                    new OnClickAction()
                    {
                        @Override
                        public void onClick()
                        {
                            loadCurrentLocation();
                        }
                    });
            mUpdateListener.onUpdate(new ShowSingle<>(item, newQuery));
        }
        else
        {
            mUpdateListener.onUpdate(new Clear<ListItem>(newQuery));
        }
    }


    private void cacheCurrentLocation()
    {
        //noinspection MissingPermission
        new GoogleApiTask<>(new GetLastLocationRequest(), new GoogleApiTask.Callback<GeoLocation>()
        {
            @Override
            public void onTaskFinish(GoogleApiTask.Result<GeoLocation> result)
            {
                try
                {
                    new GetCityTask<>(new ParcelableGeoLocation(result.value()), /* no callback needed */ null, mCurrentCityCache)
                            .executeOnExecutor(mExecutorService, mGeocoderFactory.get());
                }
                catch (Exception e)
                {
                    // Do nothing, we couldn't cache the value, but there will be proper error handling when user taps on the items
                }

            }
        }).executeOnExecutor(mExecutorService, mGoogleApis);
    }


    private void loadCurrentLocation()
    {
        //noinspection MissingPermission
        new GoogleApiTask<>(new GetLastLocationRequest(), new GoogleApiTask.Callback<GeoLocation>()
        {
            @Override
            public void onTaskFinish(GoogleApiTask.Result<GeoLocation> result)
            {
                try
                {
                    onLastLocationReceived(result.value());
                }
                catch (GoogleApiAutoManagedIssueException e)
                {
                    Log.e(TAG, "Failed to get last location", e);
                    // Do nothing, being auto-managed, module will be triggered in onResume()
                }
                catch (GoogleApiNonRecoverableException e)
                {
                    Log.e(TAG, "Failed to get last location", e);
                    mUpdateListener.onUpdate(new ForcedClear<ListItem>());
                }
                catch (GoogleApiRecoverableException e)
                {
                    Log.e(TAG, "Failed to get last location", e);
                    ListItem errorItem = new ButtonedMessageItem(
                            mActivity.getText(R.string.schedjoules_location_picker_googleapi_error_recovarable),
                            mActivity.getText(R.string.schedjoules_retry),
                            new ActivityReloadAction(mActivity));
                    mUpdateListener.onUpdate(new ShowSingle<>(errorItem, ""));
                }
                catch (GoogleApiExecutionException e)
                {
                    Log.e(TAG, "Failed to get last location", e);
                    onFailedToGetLocation();
                }
                catch (AbstractGoogleApiRequestException e)
                {
                    throw new RuntimeException("Unhandled AbstractGoogleApiRequestException");
                }
            }
        }).executeOnExecutor(mExecutorService, mGoogleApis);
    }


    private void onLastLocationReceived(GeoLocation result)
    {
        new GetCityTask<>(new ParcelableGeoLocation(result), new GetCityTask.Client<ParcelableGeoLocation>()
        {
            @Override
            public void onTaskFinish(SafeAsyncTaskResult<GeoPlace> result, ParcelableGeoLocation location)
            {
                try
                {
                    mItemChosenAction.onItemChosen(result.value());
                }
                catch (Exception e)
                {
                    Log.e(TAG, "GetCityTask failed", e);
                    onFailedToGetLocation();
                }
            }
        }, mCurrentCityCache).executeOnExecutor(mExecutorService, mGeocoderFactory.get());
    }


    private void onFailedToGetLocation()
    {
        ListItem errorItem = new ButtonedMessageItem(
                mActivity.getString(R.string.schedjoules_location_picker_current_location_error),
                mActivity.getString(R.string.schedjoules_retry),
                new RetryAction());
        mUpdateListener.onUpdate(new ShowSingle<>(errorItem, ""));
    }


    private final class RetryAction implements OnClickAction
    {

        @Override
        public void onClick()
        {
            onSearchQueryChange("");
        }
    }
}
