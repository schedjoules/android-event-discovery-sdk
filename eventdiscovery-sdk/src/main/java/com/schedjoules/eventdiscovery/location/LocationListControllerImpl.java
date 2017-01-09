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

package com.schedjoules.eventdiscovery.location;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.AdapterNotifier;
import com.schedjoules.eventdiscovery.framework.adapter.ListItem;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskResult;
import com.schedjoules.eventdiscovery.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.location.model.NamedPlace;
import com.schedjoules.eventdiscovery.utils.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The implementation for {@link LocationListController}.
 *
 * @author Gabor Keszthelyi
 */
public final class LocationListControllerImpl implements LocationListController, LocationSuggestionItem.OnClickListener
{
    private static final String TAG = LocationListControllerImpl.class.getSimpleName();

    private final GoogleApiClient mApiClient;
    private final ExecutorService mExecutorService;
    private final PlaceSuggestionQueryTask.Client mTaskClient;

    private AdapterNotifier mAdapterNotifier;
    private PlaceSelectedListener mPlaceSelectedListener;

    private List<ListItem> mListItems = new ArrayList<>();
    private String mLastQuery;


    public LocationListControllerImpl(GoogleApiClient apiClient)
    {
        mApiClient = apiClient;
        mExecutorService = Executors.newSingleThreadExecutor();
        mTaskClient = new PlaceSuggestionQueryTaskClient();
    }


    @Override
    public void query(String query)
    {
        mLastQuery = query;
        new PlaceSuggestionQueryTask(query, mTaskClient).executeOnExecutor(mExecutorService, mApiClient);
    }


    @Override
    public void setAdapterNotifier(AdapterNotifier adapterNotifier)
    {
        mAdapterNotifier = adapterNotifier;
    }


    @Override
    public void setOnPlaceSelectedListener(PlaceSelectedListener listener)
    {
        mPlaceSelectedListener = listener;
    }


    @Override
    public ListItem get(int position)
    {
        return mListItems.get(position);
    }


    @Override
    public int itemCount()
    {
        return mListItems.size();
    }


    @Override
    public void onPlaceSuggestionSelected(NamedPlace namedPlace)
    {
        new PlaceByIdTask(namedPlace, new PlaceByIdTaskClient()).executeOnExecutor(mExecutorService, mApiClient);
    }


    private class PlaceSuggestionQueryTaskClient implements PlaceSuggestionQueryTask.Client
    {

        @Override
        public boolean shouldDiscard(String query)
        {
            return !Objects.equals(query, mLastQuery);
        }


        @Override
        public void onTaskFinish(SafeAsyncTaskResult<List<ListItem>> taskResult, String query)
        {
            try
            {
                onTaskSuccess(taskResult.value());
            }
            catch (Exception e)
            {
                onTaskFailed(e);
            }
        }


        private void onTaskSuccess(List<ListItem> newItems)
        {
            for (ListItem newItem : newItems)
            {
                if (newItem instanceof LocationSuggestionItem)
                {
                    ((LocationSuggestionItem) newItem).setListener(LocationListControllerImpl.this);
                }
            }

            int sizeBefore = mListItems.size();
            mListItems = newItems;
            mAdapterNotifier.notifyItemsCleared(sizeBefore);
            mAdapterNotifier.notifyNewItemsAdded(mListItems, 0);
        }


        private void onTaskFailed(Exception e)
        {
            // TODO UI
            Log.e(TAG, "Error returned by places suggestion query task.", e);
        }
    }


    private class PlaceByIdTaskClient implements PlaceByIdTask.Client
    {

        @Override
        public void onTaskFinish(SafeAsyncTaskResult<GeoPlace> result, NamedPlace namedPlace)
        {
            try
            {
                mPlaceSelectedListener.onPlaceSelected(result.value());
            }
            catch (Exception e)
            {
                // TODO UI
                Log.e(TAG, "Error returned by place by id task.", e);
            }
        }
    }
}
