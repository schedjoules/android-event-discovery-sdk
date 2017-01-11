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

package com.schedjoules.eventdiscovery.framework.location;

import com.schedjoules.client.eventsdiscovery.GeoLocation;


/**
 * {@link LocationSelectionResult} that simply takes the properties as constructor parameters.
 *
 * @author Gabor Keszthelyi
 */
public final class StructuredLocationSelectionResult implements LocationSelectionResult
{
    private final CharSequence mName;
    private final GeoLocation mGeoLocation;


    public StructuredLocationSelectionResult(CharSequence name, GeoLocation geoLocation)
    {
        mName = name;
        mGeoLocation = geoLocation;
    }


    @Override
    public CharSequence name()
    {
        return mName;
    }


    @Override
    public GeoLocation geoLocation()
    {
        return mGeoLocation;
    }
}
