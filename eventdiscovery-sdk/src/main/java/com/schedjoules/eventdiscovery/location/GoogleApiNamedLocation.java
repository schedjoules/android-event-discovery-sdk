/*
 * Copyright 2016 SchedJoules
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

import com.google.android.gms.location.places.Place;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.locations.StructuredGeoLocation;


/**
 * {@link NamedLocation} adapting a {@link Place}.
 *
 * @author Gabor Keszthelyi
 */
public final class GoogleApiNamedLocation implements NamedLocation
{
    private final Place mPlace;


    public GoogleApiNamedLocation(Place place)
    {
        mPlace = place;
    }


    @Override
    public CharSequence name()
    {
        return mPlace.getName();
    }


    @Override
    public GeoLocation geoLocation()
    {
        return new StructuredGeoLocation((float) mPlace.getLatLng().latitude, (float) mPlace.getLatLng().longitude);
    }
}
