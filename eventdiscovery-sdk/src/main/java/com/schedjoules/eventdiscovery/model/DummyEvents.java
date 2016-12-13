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

package com.schedjoules.eventdiscovery.model;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.Location;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.client.eventsdiscovery.queries.SimpleEventsDiscovery;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.ScrollDirection;
import com.schedjoules.eventdiscovery.utils.DateTimeFormatter;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.schedjoules.eventdiscovery.eventlist.itemsprovider.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.eventlist.itemsprovider.ScrollDirection.TOP;


/**
 * Dummy Events for development, testing.
 *
 * @author Gabor Keszthelyi
 */
/*
Use in EventListDownloadTask.doInBackgroundWithException():

 // ResultPage<Envelope<Event>> resultPage = apiService.service(1000).apiResponse(taskParam.mQuery);
 ResultPage<Envelope<Event>> resultPage = DummyEvents.resultPage(taskParam.mDirection);
*/
public final class DummyEvents
{
    private static final String TAG = "Network";

    static Map<ScrollDirection, Integer> pageLimits = new HashMap<>();
    static Map<ScrollDirection, Integer> eventCounters = new HashMap<>();
    static Map<ScrollDirection, Integer> pageCounters = new HashMap<>();
    static Map<ScrollDirection, Pair<Integer, Integer>> errorOccurrences = new HashMap<>();

    static final int HOURS_BETWEEN_EVENTS = 5;
    static final int NO_OF_EVENTS_PER_PAGE = 1;

    static
    {
        init();
    }

    public static void init()
    {
        pageCounters.put(TOP, 0);
        pageCounters.put(BOTTOM, 0);

        eventCounters.put(TOP, 0);
        eventCounters.put(BOTTOM, 0);

        pageLimits.put(TOP, 8);
        pageLimits.put(BOTTOM, 10);

        errorOccurrences.put(TOP, Pair.create(6, 3));
        errorOccurrences.put(BOTTOM, Pair.create(8, 2));
    }


    public static ResultPage<Envelope<Event>> resultPage(ScrollDirection direction)
    {
        Log.d(TAG, String.format("DummyRequest: dir %s, page %s", direction, pageCounters.get(direction)));
        sleep(2000);

        if (pageCounters.get(direction).equals(errorOccurrences.get(direction).first)
                && (errorOccurrences.get(direction).second > 0))
        {
            errorOccurrences.put(direction,
                    Pair.create(errorOccurrences.get(direction).first, errorOccurrences.get(direction).second - 1));
            throw new RuntimeException(String.format("Induced error. Direction %s Page %s Occurence %s", direction,
                    pageCounters.get(direction), errorOccurrences.get(direction).second));
        }

        if (pageLimits.get(direction) <= pageCounters.get(direction))
        {
            return new EmptyResultPage(direction);
        }
        return new DummyResultPage(envelopes(direction));
    }


    private static void sleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
        }
    }


    private static Iterable<Envelope<Event>> envelopes(ScrollDirection direction)
    {
        List<Envelope<Event>> result = new ArrayList<>();

        for (int i = 0; i < NO_OF_EVENTS_PER_PAGE; i++)
        {
            result.add(dummy(DateTime.now().addDuration(duration(direction))));
            eventCounters.put(direction, eventCounters.get(direction) + 1);
        }
        pageCounters.put(direction, pageCounters.get(direction) + 1);

        if (direction == TOP)
        {
            Collections.reverse(result);
        }

        return result;
    }


    @NonNull
    private static Duration duration(ScrollDirection direction)
    {
        int sign = direction == BOTTOM ? 1 : -1;
        int multiplier = direction == BOTTOM ? eventCounters.get(direction) : eventCounters.get(direction) + 1;
        return new Duration(sign, 0, multiplier * HOURS_BETWEEN_EVENTS, 0, 0);
    }


    private static Envelope<Event> dummy(DateTime start)
    {
        return new DummyEnvelope(new DummyEvent(DateTimeFormatter.mediumLongDateFormat(start), start));
    }


    private static class DummyResultPage implements ResultPage<Envelope<Event>>
    {
        private final Iterable<Envelope<Event>> mItems;


        private DummyResultPage(Iterable<Envelope<Event>> items)
        {
            mItems = items;
        }


        @Override
        public Iterable<Envelope<Event>> items()
        {
            return mItems;
        }


        @Override
        public boolean isFirstPage()
        {
            return false;
        }


        @Override
        public boolean isLastPage()
        {
            return false;
        }


        @Override
        public ApiQuery<ResultPage<Envelope<Event>>> previousPageQuery() throws IllegalStateException
        {
            return new SimpleEventsDiscovery();
        }


        @Override
        public ApiQuery<ResultPage<Envelope<Event>>> nextPageQuery() throws IllegalStateException
        {
            return new SimpleEventsDiscovery();
        }
    }


    private static class DummyEnvelope implements Envelope<Event>
    {
        private final Event mEvent;


        private DummyEnvelope(Event event)
        {
            mEvent = event;
        }


        @Override
        public String etag()
        {
            return "dummy etag";
        }


        @Override
        public String uid()
        {
            return "dummy env uid";
        }


        @Override
        public boolean hasPayload()
        {
            return true;
        }


        @Override
        public Event payload()
        {
            return mEvent;
        }
    }


    private static class DummyEvent implements Event
    {

        private final String mTitle;
        private final DateTime mStart;


        private DummyEvent(String title, DateTime start)
        {
            mTitle = title;
            mStart = start;
        }


        @Override
        public String uid()
        {
            return "dummy uid";
        }


        @Override
        public DateTime start()
        {
            return mStart;
        }


        @Override
        public Duration duration()
        {
            return new Duration(1, 0, 2, 0, 0);
        }


        @Override
        public String title()
        {
            return mTitle;
        }


        @Override
        public String description()
        {
            return "dummy desc";
        }


        @Override
        public Iterable<Location> locations()
        {
            return Collections.emptyList();
        }


        @Override
        public Iterable<Link> links()
        {
            return Collections.emptyList();
        }
    }


    private static class EmptyResultPage implements ResultPage<Envelope<Event>>
    {
        private final ScrollDirection mDirection;


        public EmptyResultPage(ScrollDirection direction)
        {
            mDirection = direction;
        }


        @Override
        public Iterable<Envelope<Event>> items()
        {
            return Collections.emptyList();
        }


        @Override
        public boolean isFirstPage()
        {
            return mDirection == TOP;
        }


        @Override
        public boolean isLastPage()
        {
            return mDirection == BOTTOM;
        }


        @Override
        public ApiQuery<ResultPage<Envelope<Event>>> previousPageQuery() throws IllegalStateException
        {
            if (mDirection == TOP)
            {
                throw new IllegalStateException();
            }
            return new SimpleEventsDiscovery();
        }


        @Override
        public ApiQuery<ResultPage<Envelope<Event>>> nextPageQuery() throws IllegalStateException
        {
            if (mDirection == BOTTOM)
            {
                throw new IllegalStateException();
            }
            return new SimpleEventsDiscovery();
        }
    }
}
