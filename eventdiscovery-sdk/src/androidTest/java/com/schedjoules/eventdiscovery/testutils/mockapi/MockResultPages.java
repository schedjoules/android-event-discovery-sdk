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

package com.schedjoules.eventdiscovery.testutils.mockapi;

import android.util.Log;

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection;
import com.schedjoules.eventdiscovery.testutils.DummyEnvelope;
import com.schedjoules.eventdiscovery.testutils.DummyEvent;
import com.schedjoules.eventdiscovery.testutils.DummyResultPage;
import com.schedjoules.eventdiscovery.testutils.EmptyResultPage;
import com.schedjoules.eventdiscovery.testutils.mockapi.pages.ErrorMockApiPage;
import com.schedjoules.eventdiscovery.testutils.mockapi.pages.MockApiResultPage;
import com.schedjoules.eventdiscovery.testutils.mockapi.pages.SuccessMockApiPage;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.TOP;
import static com.schedjoules.eventdiscovery.testutils.Durations.reverse;


/**
 * Provides mocked event {@link ResultPage}s for UI tests.
 *
 * @author Gabor Keszthelyi
 */
public final class MockResultPages
{
    private static final String TAG = "DummyNetwork";

    private final MockPagesSetup mSetup;

    private final Map<ScrollDirection, List<MockApiResultPage>> mEventsOnPages;

    private final Map<ScrollDirection, AtomicInteger> mPageCounters = directionMap(new AtomicInteger(0),
            new AtomicInteger(0));

    private final Map<ScrollDirection, DateTime> mLastAddedEventStartTime;


    public MockResultPages(MockPagesSetup setup)
    {
        mSetup = setup;
        mEventsOnPages = directionMap(setup.mEventsOnPagesBottom, setup.mEventsOnPagesTop);
        mLastAddedEventStartTime = directionMap(
                setup.mFirstEventTime.addDuration(reverse(setup.mIntervalBetweenEvents)),
                setup.mFirstEventTime);

        validateLastPageEmpty(BOTTOM);
        validateLastPageEmpty(TOP);
    }


    private void validateLastPageEmpty(ScrollDirection direction)
    {
        MockApiResultPage lastPage = mEventsOnPages.get(direction).get(mEventsOnPages.get(direction).size() - 1);
        if (!(lastPage instanceof SuccessMockApiPage) || ((SuccessMockApiPage) lastPage).noOfEvents() != 0)
        {
            throw new IllegalArgumentException("Last page must be empty");
        }
    }


    public ResultPage<Envelope<Event>> nextResultPage(ScrollDirection direction)
    {
        int pageNumber = mPageCounters.get(direction).incrementAndGet();

        Log.d(TAG, String.format("DummyRequest: dir %s, page %s", direction, pageNumber));

        safeSleep(mSetup.mPageLoadTime);

        if (pageNumber > mEventsOnPages.get(direction).size())
        {
            throw new Error("Should not happen");
        }

        MockApiResultPage response = mEventsOnPages.get(direction).get(pageNumber - 1);

        if (response instanceof ErrorMockApiPage)
        {
            throw new RuntimeException(
                    String.format("Induced error. Direction %s PageRequest %s", direction, pageNumber));
        }

        SuccessMockApiPage successResponse = (SuccessMockApiPage) response;
        if (successResponse.noOfEvents() == 0)
        {
            return new EmptyResultPage();
        }

        return new DummyResultPage(envelopes(direction, successResponse.noOfEvents()), successResponse.isFirst(),
                successResponse.isLast());
    }


    private Iterable<Envelope<Event>> envelopes(ScrollDirection direction, int noOfEvents)
    {
        List<Envelope<Event>> result = new ArrayList<>();
        for (int i = 1; i <= noOfEvents; i++)
        {
            result.add(dummyEnvelope(direction, i));
        }
        return result;
    }


    private Envelope<Event> dummyEnvelope(ScrollDirection direction, int posOfEventPerPage)
    {
        DateTime lastDateTime = mLastAddedEventStartTime.get(direction);
        DateTime start = lastDateTime.addDuration(offset(direction));
        mLastAddedEventStartTime.put(direction, start);

        String title = String.format("%s P%s E%s   %s",
                direction == BOTTOM ? "B" : "T",
                mPageCounters.get(direction).get(),
                posOfEventPerPage,
                start.toString());

        return new DummyEnvelope(new DummyEvent(title, start));
    }


    private Duration offset(ScrollDirection direction)
    {
        return direction == BOTTOM ? mSetup.mIntervalBetweenEvents : reverse(mSetup.mIntervalBetweenEvents);
    }


    private void safeSleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
        }
    }


    private <T> Map<ScrollDirection, T> directionMap(T bottom, T top)
    {
        EnumMap<ScrollDirection, T> enumMap = new EnumMap<>(ScrollDirection.class);
        enumMap.put(BOTTOM, bottom);
        enumMap.put(TOP, top);
        return enumMap;
    }

}
