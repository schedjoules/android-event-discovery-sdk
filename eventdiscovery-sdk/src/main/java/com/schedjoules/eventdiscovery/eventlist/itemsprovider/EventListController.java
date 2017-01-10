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

package com.schedjoules.eventdiscovery.eventlist.itemsprovider;

import android.support.v7.widget.RecyclerView;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.eventlist.view.EdgeReachScrollListener;
import com.schedjoules.eventdiscovery.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.framework.adapter.ListItems;

import org.dmfs.rfc5545.DateTime;


/**
 * Controller for the event list, it responsible to initiate API request, manage states and update the {@link ListItems}
 * associated with the {@link RecyclerView}
 *
 * @author Gabor Keszthelyi
 */
public interface EventListController extends EdgeReachScrollListener.Listener
{

    /**
     * Initiate loading events for the given location and start time.
     */
    void loadEvents(GeoLocation geoLocation, DateTime dateTime);

    void setAdapterNotifier(AdapterNotifier adapterNotifier);

    void setBackgroundMessageUI(EventListBackgroundMessage backgroundMessage);

    void setLoadingIndicatorUI(EventListLoadingIndicatorOverlay loadingIndicatorOverlay);

}
