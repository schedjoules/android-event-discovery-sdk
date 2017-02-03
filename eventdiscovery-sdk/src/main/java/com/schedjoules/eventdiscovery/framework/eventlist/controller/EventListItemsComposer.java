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

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.eventlist.items.DateHeaderItem;
import com.schedjoules.eventdiscovery.framework.eventlist.items.EventItem;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.flexibleadapter.FlexibleHeaderItemAdapter;
import com.schedjoules.eventdiscovery.framework.list.flexibleadapter.FlexibleSectionableAdapter;
import com.schedjoules.eventdiscovery.framework.list.flexibleadapter.HeaderFlexibleViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;


/**
 * Composes the list items to show on the UI in the event list from the actual event list data.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListItemsComposer
{

    public static final EventListItemsComposer INSTANCE = new EventListItemsComposer();


    private EventListItemsComposer()
    {

    }


    /**
     * Creates {@link ListItem}s for the event list UI that represent the given events fetched from the api.
     *
     * @param resultPage
     *         the result with envelopes fetched from the api
     *
     * @return the list of {@link ListItem} that can be added to the RecyclerView
     */
    public List<IFlexible> compose(ResultPage<Envelope<Event>> resultPage)
    {
        if (resultPage == null)
        {
            return new ArrayList<>(0);
        }

        List<IFlexible> items = new ArrayList<>();

        // DateHeaderItem is used as value object here as the key for the Map,
        // but we need the reference to the actual object as well, so the need for the Map instead of a Set.
        Map<IHeader<HeaderFlexibleViewHolder>, IHeader<HeaderFlexibleViewHolder>> headers = new HashMap<>();

        for (Envelope<Event> envelope : resultPage.items())
        {
            if (envelope.hasPayload())
            {
                Event event = envelope.payload();
//                Log.d("network", String.format("event | %s | %s",
//                        new SimpleDateFormat("M dd HH:mm").format(event.start().getTimestamp()), event.title()));

                IHeader header = new FlexibleHeaderItemAdapter<>(new DateHeaderItem(event.start()));

                if (!headers.containsKey(header))
                {
                    headers.put(header, header);
                }

                ISectionable sectionItem = new FlexibleSectionableAdapter<>(
                        new EventItem(event), headers.get(header));
                items.add(sectionItem);
            }
        }

        return items;
    }

}
