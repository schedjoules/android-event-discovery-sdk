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

package com.schedjoules.eventdiscovery.location.tasks;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTask;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.location.model.GoogleGeoLocation;
import com.schedjoules.eventdiscovery.location.model.NamedPlace;
import com.schedjoules.eventdiscovery.location.model.StructuredGeoPlace;


/**
 * TODO maybe generalize, and rename Google, but not sure if other services would have this workflow
 *
 * @author Gabor Keszthelyi
 */
// TODO could be prefetched and cached, not sure if it's worth it, but could be done in a separate executor that has many threads
public final class PlaceByIdTask extends SafeAsyncTask<NamedPlace, GoogleApiClient, Void, GeoPlace>
{
    public PlaceByIdTask(NamedPlace namedPlace, Client client)
    {
        super(namedPlace, client);
    }


    @Override
    protected GeoPlace doInBackgroundWithException(NamedPlace namedPlace, GoogleApiClient... googleApiClients) throws Exception
    {
        GoogleApiClient googleApiClient = googleApiClients[0];

        PendingResult<PlaceBuffer> placeById = Places.GeoDataApi.getPlaceById(googleApiClient, namedPlace.id());
        PlaceBuffer placeBuffer = placeById.await();
        Place place = placeBuffer.get(0);

        return new StructuredGeoPlace(namedPlace, new GoogleGeoLocation(place));
    }


    public interface Client extends SafeAsyncTaskCallback<NamedPlace, GeoPlace>
    {

    }
}
