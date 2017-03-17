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
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskResult;
import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.smart.Clickable;
import com.schedjoules.eventdiscovery.framework.location.listitems.MessageItem;
import com.schedjoules.eventdiscovery.framework.location.model.AndroidGeoLocation;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.tasks.GetCityTask;
import com.schedjoules.eventdiscovery.framework.model.ParcelableGeoLocation;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.Clear;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ShowSingle;
import com.schedjoules.eventdiscovery.framework.utils.cache.Cache;
import com.schedjoules.eventdiscovery.framework.utils.cache.TimedSingleValueCache;
import com.schedjoules.eventdiscovery.framework.utils.factory.Caching;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;
import com.schedjoules.eventdiscovery.framework.utils.strings.BasicStrings;
import com.schedjoules.eventdiscovery.framework.utils.strings.Strings;

import java.util.Locale;
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
    public static final SearchModuleFactory<GeoPlace> FACTORY = new SearchModuleFactory<GeoPlace>()
    {
        @Override
        public SearchModule create(final Activity activity, ResultUpdateListener<ListItem> updateListener, ItemChosenAction<GeoPlace> itemChosenAction)
        {
            GoogleApiClient googleApiClient = new GoogleApiClient
                    .Builder(activity)
                    .addApi(LocationServices.API)
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

            Factory<Geocoder> geocoderFactory = new Caching<>(new Factory<Geocoder>()
            {
                @Override
                public Geocoder create()
                {
                    return new Geocoder(activity, Locale.getDefault());
                }
            });

            return new CurrentLocationModule(googleApiClient, updateListener, itemChosenAction, geocoderFactory,
                    new BasicStrings(activity));
        }
    };

    private static final String TAG = "CurrentLocationModule";

    private final GoogleApiClient mGoogleApiClient;
    private final ResultUpdateListener<ListItem> mUpdateListener;
    private final ItemChosenAction<GeoPlace> mItemChosenAction;
    private final Factory<Geocoder> mGeocoderFactory;
    private final Strings mStrings;

    private final ExecutorService mExecutorService;

    private final GoogleApiConnectionCallback mApiConnectionCallback;
    private final GetCityTaskClient mGetCityTaskClient;

    private final Cache<ParcelableGeoLocation, GeoPlace> mCurrentCityCache;


    public CurrentLocationModule(GoogleApiClient googleApiClient,
                                 ResultUpdateListener<ListItem> updateListener,
                                 ItemChosenAction<GeoPlace> itemChosenAction,
                                 Factory<Geocoder> geocoderFactory,
                                 Strings strings)
    {
        mGoogleApiClient = googleApiClient;
        mUpdateListener = updateListener;
        mItemChosenAction = itemChosenAction;
        mGeocoderFactory = geocoderFactory;
        mStrings = strings;

        mExecutorService = Executors.newSingleThreadExecutor();

        mApiConnectionCallback = new GoogleApiConnectionCallback();
        mGetCityTaskClient = new GetCityTaskClient();
        mCurrentCityCache = new TimedSingleValueCache<>(3, TimeUnit.MINUTES);
    }


    @Override
    public void shutDown()
    {
        mGoogleApiClient.unregisterConnectionCallbacks(mApiConnectionCallback);
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onSearchQueryChange(String newQuery)
    {
        if (!newQuery.isEmpty())
        {
            mUpdateListener.onUpdate(new Clear<ListItem>(newQuery));
        }
        else
        {
            ListItem loadingItem = new MessageItem(mStrings.get(R.string.schedjoules_location_picker_current_location_locating));
            mUpdateListener.onUpdate(new ShowSingle<>(loadingItem, ""));

            // Note: If already connected, onConnected() is called immediately.
            mGoogleApiClient.registerConnectionCallbacks(mApiConnectionCallback);
        }
    }


    private void onCityReceived(final GeoPlace city)
    {
        ListItem item = new Clickable<>(
                new MessageItem(mStrings.get(R.string.schedjoules_location_picker_current_location)),
                new OnClickAction()
                {
                    @Override
                    public void onClick()
                    {
                        mItemChosenAction.onItemChosen(city);
                    }
                });
        mUpdateListener.onUpdate(new ShowSingle<>(item, ""));
    }


    private void onFailure()
    {
        ListItem errorItem = new Clickable<>(
                new MessageItem(mStrings.get(R.string.schedjoules_location_picker_current_location_error)),
                new OnClickAction()
                {
                    @Override
                    public void onClick()
                    {
                        // Retry:
                        onSearchQueryChange("");
                    }
                }
        );
        mUpdateListener.onUpdate(new ShowSingle<>(errorItem, ""));
    }


    private class GoogleApiConnectionCallback implements GoogleApiClient.ConnectionCallbacks
    {

        @Override
        public void onConnected(@Nullable Bundle bundle)
        {
            @SuppressWarnings("MissingPermission")
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (lastLocation != null)
            {
                ParcelableGeoLocation geoLocation = new ParcelableGeoLocation(new AndroidGeoLocation(lastLocation));
                new GetCityTask<>(geoLocation, mGetCityTaskClient, mCurrentCityCache).executeOnExecutor(mExecutorService, mGeocoderFactory.create());
            }
            else
            {
                Log.e(TAG, "Last location is null");
                onFailure();
            }
        }


        @Override
        public void onConnectionSuspended(int i)
        {
            // Based on the documentation ("GoogleApiClient will automatically attempt to restore the connection...  wait for onConnected()")
            // we probably should do nothing here. Extra state would be needed otherwise to check
            // whether we already have the location displayed or not.
        }
    }


    private class GetCityTaskClient implements GetCityTask.Client<ParcelableGeoLocation>
    {

        @Override
        public void onTaskFinish(SafeAsyncTaskResult<GeoPlace> result, ParcelableGeoLocation location)
        {
            try
            {
                onCityReceived(result.value());
            }
            catch (Exception e)
            {
                Log.e(TAG, "GetCityTask failed", e);
                onFailure();
            }

        }

    }
}
