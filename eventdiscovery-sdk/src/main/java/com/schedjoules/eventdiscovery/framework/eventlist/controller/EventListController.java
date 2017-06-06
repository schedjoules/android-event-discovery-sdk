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

package com.schedjoules.eventdiscovery.framework.eventlist.controller;

import android.support.v7.widget.RecyclerView;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EdgeReachScrollListener;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.framework.list.ListItems;


/**
 * Controller for the event list, it responsible to initiate API request, manage states and update the {@link ListItems} associated with the {@link
 * RecyclerView}
 *
 * @author Gabor Keszthelyi
 */
public interface EventListController extends EdgeReachScrollListener.Listener
{

    /**
     * Initiate loading events for the given location and start time.
     */
    void loadEvents(ApiQuery<ResultPage<Envelope<Event>>> query);

    void showEvents(ResultPage<Envelope<Event>> resultPage);

    void setAdapter(RecyclerView.Adapter adapter);

    void setBackgroundMessageUI(EventListBackgroundMessage backgroundMessage);

    void setLoadingIndicatorUI(EventListLoadingIndicatorOverlay loadingIndicatorOverlay);

}
