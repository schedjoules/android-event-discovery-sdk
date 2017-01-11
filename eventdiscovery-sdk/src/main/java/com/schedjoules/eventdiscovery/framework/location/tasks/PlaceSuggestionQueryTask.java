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

package com.schedjoules.eventdiscovery.framework.location.tasks;

import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.framework.async.DiscardCheck;
import com.schedjoules.eventdiscovery.framework.async.DiscardingSafeAsyncTask;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.location.PlaceSuggestionItem;
import com.schedjoules.eventdiscovery.framework.location.model.GooglePredictionNamedPlace;

import java.util.ArrayList;
import java.util.List;


/**
 * {@link AsyncTask} to query Google Places API for place suggestions.
 *
 * @author Gabor Keszthelyi
 */
public final class PlaceSuggestionQueryTask extends DiscardingSafeAsyncTask<String, GoogleApiClient, Void, List<ListItem>>
{

    private static final AutocompleteFilter CITIES_FILTER = new AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
            .build();


    public PlaceSuggestionQueryTask(String query, Client client)
    {
        super(query, client, client);
    }


    @Override
    protected List<ListItem> doInBackgroundWithException(String query, GoogleApiClient... googleApiClients) throws Exception
    {
        GoogleApiClient googleApiClient = googleApiClients[0];

        AutocompletePredictionBuffer predictions =
                Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, null, CITIES_FILTER).await();

        List<ListItem> newItems = new ArrayList<>();
        for (AutocompletePrediction prediction : predictions)
        {
            newItems.add(new PlaceSuggestionItem(new GooglePredictionNamedPlace(prediction)));
        }

        return newItems;
    }


    public interface Client extends SafeAsyncTaskCallback<String, List<ListItem>>, DiscardCheck<String>
    {

    }
}
