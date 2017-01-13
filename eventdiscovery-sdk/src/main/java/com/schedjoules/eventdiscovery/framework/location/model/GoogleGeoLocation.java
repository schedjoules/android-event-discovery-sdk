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

package com.schedjoules.eventdiscovery.framework.location.model;

import com.google.android.gms.location.places.Place;
import com.schedjoules.client.eventsdiscovery.GeoLocation;


/**
 * {@link GeoLocation} that adapts Google's {@link Place}.
 *
 * @author Gabor Keszthelyi
 */
public final class GoogleGeoLocation implements GeoLocation
{

    private final Place mPlace;


    public GoogleGeoLocation(Place place)
    {
        mPlace = place;
    }


    @Override
    public float latitude()
    {
        return (float) mPlace.getLatLng().latitude;
    }


    @Override
    public float longitude()
    {
        return (float) mPlace.getLatLng().longitude;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        GoogleGeoLocation that = (GoogleGeoLocation) o;

        if (Float.compare(that.latitude(), latitude()) != 0)
        {
            return false;
        }
        return Float.compare(that.longitude(), longitude()) == 0;

    }


    @Override
    public int hashCode()
    {
        float latitude = latitude();
        float longitude = longitude();

        int result = (latitude != +0.0f ? Float.floatToIntBits(latitude) : 0);
        result = 31 * result + (longitude != +0.0f ? Float.floatToIntBits(longitude) : 0);
        return result;
    }
}
