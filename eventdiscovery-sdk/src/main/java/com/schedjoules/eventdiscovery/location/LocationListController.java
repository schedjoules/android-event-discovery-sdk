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

import com.schedjoules.eventdiscovery.eventlist.itemsprovider.AdapterNotifier;
import com.schedjoules.eventdiscovery.framework.adapter.ListItems;
import com.schedjoules.eventdiscovery.location.model.GeoPlace;


/**
 * The controller for the location list, it initiates the network queries and updates the list items.
 *
 * @author Gabor Keszthelyi
 */
public interface LocationListController extends ListItems
{

    void query(String query);

    void setAdapterNotifier(AdapterNotifier adapterNotifier);

    void setOnPlaceSelectedListener(PlaceSelectedListener listener);

    interface PlaceSelectedListener
    {
        void onPlaceSelected(GeoPlace geoPlace);
    }

}
