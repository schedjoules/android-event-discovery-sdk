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

package com.schedjoules.eventdiscovery.framework.googleapis.requests;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApiRequest;
import com.schedjoules.eventdiscovery.framework.model.location.namedplace.GooglePredictionNamedPlace;
import com.schedjoules.eventdiscovery.framework.model.location.namedplace.NamedPlace;

import java.util.ArrayList;
import java.util.List;


/**
 * Request to get autocomplete predictions.
 *
 * @author Gabor Keszthelyi
 */
public final class GetAutoCompletePredictionsRequest implements GoogleApiRequest<List<NamedPlace>>
{
    private static final AutocompleteFilter CITIES_FILTER = new AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
            .build();

    private final String mQuery;


    public GetAutoCompletePredictionsRequest(String query)
    {
        mQuery = query;
    }


    @Override
    public Api requiredApi()
    {
        return Places.GEO_DATA_API;
    }


    @Override
    public List<NamedPlace> execute(GoogleApiClient googleApiClient)
    {
        AutocompletePredictionBuffer predictionBuffer =
                Places.GeoDataApi.getAutocompletePredictions(googleApiClient, mQuery, null, CITIES_FILTER).await();

        if (!predictionBuffer.getStatus().isSuccess())
        {
            throw new RuntimeException("Error response for AutocompletePredictions, Status: " + predictionBuffer.getStatus());
        }

        List<NamedPlace> places = new ArrayList<>();
        for (AutocompletePrediction prediction : predictionBuffer)
        {
            AutocompletePrediction frozen = prediction.freeze();
            places.add(new GooglePredictionNamedPlace(frozen));
        }
        predictionBuffer.release();

        return places;
    }
}
