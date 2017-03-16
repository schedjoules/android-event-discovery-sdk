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

import android.Manifest;
import android.location.Location;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApiRequest;
import com.schedjoules.eventdiscovery.framework.location.model.AndroidGeoLocation;


/**
 * Request to get the last known location.
 *
 * @author Gabor Keszthelyi
 */
public final class GetLastLocationRequest implements GoogleApiRequest<GeoLocation>
{

    @RequiresPermission(
            anyOf = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }
    )
    public GetLastLocationRequest()
    {
    }


    @Override
    public GeoLocation execute(GoogleApiClient googleApiClient)
    {
        @SuppressWarnings("MissingPermission")
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation == null)
        {
            throw new RuntimeException("Last location is null");
        }

        return new AndroidGeoLocation(lastLocation);
    }


    @Override
    public Api requiredApi()
    {
        return LocationServices.API;
    }
}
