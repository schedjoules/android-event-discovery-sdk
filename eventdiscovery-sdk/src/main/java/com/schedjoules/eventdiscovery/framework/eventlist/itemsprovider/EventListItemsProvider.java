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

package com.schedjoules.eventdiscovery.framework.eventlist.itemsprovider;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.framework.adapter.ListItemsProvider;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EdgeReachScrollListener;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListLoadingIndicatorOverlay;

import org.dmfs.rfc5545.DateTime;


/**
 * {@link ListItemsProvider} for Event list adapter which also listens to scrolled to bottom events to be able to load more events.
 *
 * @author Gabor Keszthelyi
 */
public interface EventListItemsProvider extends EdgeReachScrollListener.Listener
{

    /**
     * Initiate loading events for the given location and start time.
     */
    void loadEvents(GeoLocation geoLocation, DateTime dateTime);

    void setAdapterNotifier(AdapterNotifier adapterNotifier);

    void setBackgroundMessageUI(EventListBackgroundMessage backgroundMessage);

    void setLoadingIndicatorUI(EventListLoadingIndicatorOverlay loadingIndicatorOverlay);

}
