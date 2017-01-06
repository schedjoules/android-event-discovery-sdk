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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTask;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;


/** TODO
 * @author Gabor Keszthelyi
 */
// TODO could be prefetched and cached, not sure if it's worth it, but could be done in a separate executor that has many threads
public final class PlaceByIdTask extends SafeAsyncTask<String, GoogleApiClient, Void, Place>
{
    public PlaceByIdTask(String placeId, Client client)
    {
        super(placeId, client);
    }


    @Override
    protected Place doInBackgroundWithException(String placeId, GoogleApiClient... googleApiClients) throws Exception
    {
        GoogleApiClient googleApiClient = googleApiClients[0];

        PendingResult<PlaceBuffer> placeById = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);

        PlaceBuffer placeBuffer = placeById.await();
        Place place = placeBuffer.get(0);
        return place;

    }


    public interface Client extends SafeAsyncTaskCallback<String, Place>
    {

    }
}
