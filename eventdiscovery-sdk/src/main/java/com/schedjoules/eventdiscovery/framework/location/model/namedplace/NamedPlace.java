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

package com.schedjoules.eventdiscovery.framework.location.model.namedplace;

/**
 * Represents a place (city, venue, place) with a name and identifier.
 *
 * @author Gabor Keszthelyi
 */
public interface NamedPlace
{

    /**
     * The unique id for this place.
     */
    String id();

    /**
     * The main name of the place. For a city, it's the city name. For other place it's the name of the place.
     */
    CharSequence name();

    /**
     * The extra context for the place. For a city, it's the country. For other place it's mostly the address, including the city.
     */
    CharSequence extraContext();
}
