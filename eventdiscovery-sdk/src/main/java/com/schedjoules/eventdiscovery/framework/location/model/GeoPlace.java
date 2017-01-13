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

import com.schedjoules.client.eventsdiscovery.GeoLocation;


/**
 * Represents a place (city, venue, place) with geo location.
 *
 * @author Gabor Keszthelyi
 */
public interface GeoPlace
{
    NamedPlace namedPlace();

    GeoLocation geoLocation();
}
