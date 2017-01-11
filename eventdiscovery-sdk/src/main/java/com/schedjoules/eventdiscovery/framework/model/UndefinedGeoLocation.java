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

package com.schedjoules.eventdiscovery.framework.model;

import com.schedjoules.client.eventsdiscovery.GeoLocation;


/**
 * Represents a not defined {@link GeoLocation} to be able to use equals() for this case as well.
 *
 * @author Gabor Keszthelyi
 */
public final class UndefinedGeoLocation implements GeoLocation
{
    public static final GeoLocation INSTANCE = new UndefinedGeoLocation();


    private UndefinedGeoLocation()
    {

    }


    @Override
    public float latitude()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public float longitude()
    {
        throw new UnsupportedOperationException();
    }

}
