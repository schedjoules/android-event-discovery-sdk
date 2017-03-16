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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApiRequest;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.GoogleGeoLocation;
import com.schedjoules.eventdiscovery.framework.location.model.StructuredGeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.NamedPlace;


/**
 * Request to get the full Google Place by for a Place id.
 *
 * @author Gabor Keszthelyi
 */
public final class GetPlaceByIdRequest implements GoogleApiRequest<GeoPlace>
{
    private final NamedPlace mNamedPlace;


    public GetPlaceByIdRequest(NamedPlace namedPlace)
    {
        mNamedPlace = namedPlace;
    }


    @Override
    public Api requiredApi()
    {
        return Places.GEO_DATA_API;
    }


    @Override
    public GeoPlace execute(GoogleApiClient googleApiClient)
    {
        PendingResult<PlaceBuffer> pendingResult = Places.GeoDataApi.getPlaceById(googleApiClient, mNamedPlace.id());
        PlaceBuffer placeBuffer = pendingResult.await();

        if (!placeBuffer.getStatus().isSuccess())
        {
            throw new RuntimeException("Error result for GetPlaceById, Status: " + placeBuffer.getStatus());
        }

        Place frozenPlace = placeBuffer.get(0).freeze();
        placeBuffer.release();

        return new StructuredGeoPlace(mNamedPlace, new GoogleGeoLocation(frozenPlace));
    }
}
