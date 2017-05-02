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

package com.schedjoules.eventdiscovery.framework.model.location.geolocation;

import android.location.Location;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.locations.StructuredGeoLocation;


/**
 * {@link GeoLocation} adapting Android's {@link Location}.
 *
 * @author Gabor Keszthelyi
 */
public final class AndroidGeoLocation implements GeoLocation
{
    private final Location mLocation;


    public AndroidGeoLocation(Location location)
    {
        mLocation = location;
    }


    @Override
    public float latitude()
    {
        return (float) mLocation.getLatitude();
    }


    @Override
    public float longitude()
    {
        return (float) mLocation.getLongitude();
    }


    @Override
    public String toString()
    {
        return new StructuredGeoLocation(latitude(), longitude()).toString();
    }
}
