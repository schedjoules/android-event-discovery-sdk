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

import android.os.Handler;

import com.schedjoules.eventdiscovery.eventlist.items.DateHeaderItem;
import com.schedjoules.eventdiscovery.eventlist.items.EventItem;
import com.schedjoules.eventdiscovery.framework.adapter.ListItem;
import com.schedjoules.eventdiscovery.model.DummyEvent;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.schedjoules.eventdiscovery.eventlist.itemsprovider.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.eventlist.itemsprovider.ScrollDirection.TOP;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;


/**
 * Unit test for {@link EventListItems}.
 *
 * @author Gabor Keszthelyi
 */
public class EventListItemsTest
{

    private EventListItemsImpl mUnderTest;

    private AdapterNotifier mAdapterNotifier;


    @Before
    public void setup()
    {
        mUnderTest = new EventListItemsImpl(mock(Handler.class));
        mAdapterNotifier = mock(AdapterNotifier.class);
        mUnderTest.setAdapterNotifier(mAdapterNotifier);
    }


    @Test
    public void testNewItemsOnTopWithCommonDayAreMergedCorrectly()
    {
        // TODO verify AdapterNotifier calls as well

        // Initial page:

        DateTime eventStart1 = DateTime.now().addDuration(new Duration(1, 0, 2, 0, 0));
        DateHeaderItem h1orig = new DateHeaderItem(eventStart1);
        EventItem e1 = new EventItem(new DummyEvent(eventStart1));
        EventItem e2 = new EventItem(new DummyEvent(DateTime.now().addDuration(new Duration(1, 0, 4, 0, 0))));
        e1.setHeader(h1orig);
        e2.setHeader(h1orig);

        List<ListItem> newItems = new ArrayList<>();
        newItems.add(h1orig);
        newItems.add(e1);
        newItems.add(e2);

        mUnderTest.mergeNewItems(newItems, BOTTOM);

        assertSameElementsInProvider(newItems);
        assertHeaderForItem(1, h1orig);
        assertHeaderForItem(2, h1orig);

        // New page to TOP with same header:

        DateTime eventStart2 = DateTime.now().addDuration(new Duration(-1, 0, 4, 0, 0));
        DateHeaderItem h1new = new DateHeaderItem(eventStart2);
        EventItem eMinus1 = new EventItem(new DummyEvent(eventStart2));
        EventItem eMinus2 = new EventItem(new DummyEvent(DateTime.now().addDuration(new Duration(-1, 0, 2, 0, 0))));
        eMinus1.setHeader(h1new);
        eMinus2.setHeader(h1new);

        List<ListItem> newItemsTop = new ArrayList<>();
        newItemsTop.add(h1new);
        newItemsTop.add(eMinus1);
        newItemsTop.add(eMinus2);

        mUnderTest.mergeNewItems(newItemsTop, TOP);

        List<ListItem> expected = new ArrayList<>();
        expected.add(h1orig);
        expected.add(eMinus1);
        expected.add(eMinus2);
        expected.add(e1);
        expected.add(e2);
        assertSameElementsInProvider(expected);
    }


    private void assertHeaderForItem(int itemIndex, DateHeaderItem header)
    {
        assertSame(header, ((EventItem) mUnderTest.get(itemIndex)).getHeader());
    }


    private void assertSameElementsInProvider(List<ListItem> expectedItems)
    {
        assertEquals(expectedItems.size(), mUnderTest.itemCount());
        for (int i = 0; i < expectedItems.size(); i++)
        {
            assertSame(expectedItems.get(i), mUnderTest.get(i));
        }
    }

}