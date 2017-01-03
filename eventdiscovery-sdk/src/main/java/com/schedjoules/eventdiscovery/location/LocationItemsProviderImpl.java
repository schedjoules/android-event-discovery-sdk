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

import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.eventlist.items.DividerItem;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.AdapterNotifier;
import com.schedjoules.eventdiscovery.framework.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO
 *
 * @author Gabor Keszthelyi
 */
public final class LocationItemsProviderImpl implements LocationItemsProvider
{

    private final GoogleApiClient mApiClient;
    private AdapterNotifier mAdapterNotifier;

    private List<ListItem> mListItems = new ArrayList<>();


    public LocationItemsProviderImpl(GoogleApiClient apiClient)
    {
        mApiClient = apiClient;
    }


    @Override
    public void query(String queryText)
    {
        // TODO create the proper AsyncTask for the download

        final PendingResult<AutocompletePredictionBuffer> result =
                Places.GeoDataApi.getAutocompletePredictions(mApiClient, queryText,
                        null, null);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final AutocompletePredictionBuffer predictions = result.await();
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        List<ListItem> newItems = new ArrayList<>();
                        for (AutocompletePrediction prediction : predictions)
                        {
                            CharSequence fullText = prediction.getFullText(null);
                            newItems.add(new LocationSuggestionItem(fullText));
                            newItems.add(new DividerItem());
                        }

                        int sizeBefore = mListItems.size();
                        mListItems = newItems;
                        mAdapterNotifier.notifyItemsCleared(sizeBefore);
                        mAdapterNotifier.notifyNewItemsAdded(mListItems, 0);
                    }
                });
            }
        }).start();
    }


    @Override
    public void setAdapterNotifier(AdapterNotifier adapterNotifier)
    {
        mAdapterNotifier = adapterNotifier;
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
}
