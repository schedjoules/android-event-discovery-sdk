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

import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;


/**
 * Location selection controller.
 *
 * @author Gabor Keszthelyi
 */
public interface PlaceSelection
{
    /**
     * Prompt user to select a location.
     */
    void initiateSelection();

    void registerListener(Listener listener);

    void unregisterListener();

    interface Listener
    {
        /**
         * Called when the user has selected a location.
         */
        void onPlaceSelected(GeoPlace result);
    }

}
