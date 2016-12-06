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

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.eventlist.items.DateHeaderItem;
import com.schedjoules.eventdiscovery.eventlist.items.DividerItem;
import com.schedjoules.eventdiscovery.eventlist.items.EventItem;
import com.schedjoules.eventdiscovery.framework.adapter.ListItem;

import org.dmfs.rfc5545.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


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
    public List<ListItem> compose(ResultPage<Envelope<Event>> resultPage)
    {
        if (resultPage == null)
        {
            return new ArrayList<>(0);
        }

        List<ListItem> items = new ArrayList<>();

        Map<DateTime, DateHeaderItem> days = new HashMap<>();
        for (Envelope<Event> envelope : resultPage.items())
        {
            if (envelope.hasPayload())
            {
                Event event = envelope.payload();
//                Log.d("Network", String.format("event | %s | %s",
//                        new SimpleDateFormat("M dd HH:mm").format(event.start().getTimestamp()), event.title()));

                DateTime day = toLocalDay(event.start());

                if (!days.containsKey(day))
                {
                    DateHeaderItem headerItem = new DateHeaderItem(day);
                    days.put(day, headerItem);
                    items.add(headerItem);
                }
                EventItem item = new EventItem(event);
                item.setHeader(days.get(day));
                items.add(item);
                items.add(new DividerItem());
            }
        }

        return items;
    }


    private DateTime toLocalDay(DateTime dateTime)
    {
        return dateTime.shiftTimeZone(TimeZone.getDefault()).toAllDay();
    }

}
