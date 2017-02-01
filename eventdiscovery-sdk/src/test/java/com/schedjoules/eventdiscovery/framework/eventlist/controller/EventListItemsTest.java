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

import android.os.Handler;

import com.schedjoules.eventdiscovery.framework.eventlist.items.DateHeaderItem;
import com.schedjoules.eventdiscovery.framework.eventlist.items.EventItem;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.flexibleadapter.ThirdPartyAdapterNotifier;
import com.schedjoules.eventdiscovery.testutils.DummyEvent;

import org.dmfs.rfc5545.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.TOP;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Unit test for {@link EventListItemsImpl}.
 *
 * @author Gabor Keszthelyi
 */
public class EventListItemsTest
{

    private EventListItems mUnderTest;

    private ThirdPartyAdapterNotifier mAdapterNotifier;


    @Before
    public void setup()
    {
        TimeZone.setDefault(DateTime.UTC);
        mUnderTest = new EventListItemsImpl(Mockito.mock(Handler.class));
        mAdapterNotifier = Mockito.mock(ThirdPartyAdapterNotifier.class);
        mAdapterNotifier.notifyInitialItemsAdded(new ArrayList());
        mUnderTest.setAdapterNotifier(mAdapterNotifier);
    }


    @Test
    public void testNewItemsOnTopWithCommonDayAreMergedCorrectly()
    {
        // Initial page:
        // ARRANGE
        DateTime e1start = new DateTime(2016, 12 - 1, 16, 14, 0, 0);
        DateHeaderItem h1orig = new DateHeaderItem(e1start);
        EventItem e1 = new EventItem(new DummyEvent(e1start));
        EventItem e2 = new EventItem(new DummyEvent(new DateTime(2016, 12 - 1, 16, 17, 0, 0)));
        e1.setHeader(h1orig);
        e2.setHeader(h1orig);

        List<ListItem> newItems = new ArrayList<>();
        newItems.add(h1orig);
        newItems.add(e1);
        newItems.add(e2);

        // ACT
        mUnderTest.mergeNewItems(newItems, BOTTOM);

        // ASSERT
        verify(mAdapterNotifier, times(1)).notifyNewItemsAdded(newItems, 0);
        assertSameElementsInProvider(newItems);
        assertHeaderForItem(1, h1orig);
        assertHeaderForItem(2, h1orig);

        // New page to TOP with same header:
        // ARRANGE
        DateTime eventStart2 = new DateTime(2016, 12 - 1, 16, 10, 0, 0);
        DateHeaderItem h1new = new DateHeaderItem(eventStart2);
        EventItem eMinus1 = new EventItem(new DummyEvent(eventStart2));
        EventItem eMinus2 = new EventItem(new DummyEvent(new DateTime(2016, 12 - 1, 16, 11, 0, 0)));
        eMinus1.setHeader(h1new);
        eMinus2.setHeader(h1new);

        List<ListItem> newItemsTop = new ArrayList<>();
        newItemsTop.add(h1new);
        newItemsTop.add(eMinus1);
        newItemsTop.add(eMinus2);

        // ACT
        mUnderTest.mergeNewItems(newItemsTop, TOP);

        // ASSERT
        verify(mAdapterNotifier, times(1)).notifyItemRemoved(0);
        verify(mAdapterNotifier, times(1))
                .notifyNewItemsAdded(Arrays.asList(h1orig, eMinus1, eMinus2), 0);
        assertSameElementsInProvider(Arrays.<ListItem>asList(h1orig, eMinus1, eMinus2, e1, e2));
        assertHeaderForItem(1, h1orig);
        assertHeaderForItem(2, h1orig);
        assertHeaderForItem(3, h1orig);
        assertHeaderForItem(4, h1orig);
    }


    @Test
    public void testNewItemsBottomWithCommonDayAreMergedCorrectly()
    {
        // Initial page:
        // ARRANGE
        DateTime e1start = new DateTime(2016, 12 - 1, 16, 14, 0, 0);
        DateHeaderItem h1orig = new DateHeaderItem(e1start);
        EventItem e1 = new EventItem(new DummyEvent(e1start));
        EventItem e2 = new EventItem(new DummyEvent(new DateTime(2016, 12 - 1, 16, 17, 0, 0)));
        e1.setHeader(h1orig);
        e2.setHeader(h1orig);

        List<ListItem> newItems = new ArrayList<>();
        newItems.add(h1orig);
        newItems.add(e1);
        newItems.add(e2);

        // ACT
        mUnderTest.mergeNewItems(newItems, BOTTOM);

        // ASSERT
        verify(mAdapterNotifier, times(1)).notifyNewItemsAdded(newItems, 0);
        assertSameElementsInProvider(newItems);
        assertHeaderForItem(1, h1orig);
        assertHeaderForItem(2, h1orig);

        // New page to BOTTOM with same header:
        // ARRANGE
        DateTime eventStart2 = new DateTime(2016, 12 - 1, 16, 18, 0, 0);
        DateHeaderItem h1new = new DateHeaderItem(eventStart2);
        EventItem ePlus1 = new EventItem(new DummyEvent(eventStart2));
        EventItem ePlus2 = new EventItem(new DummyEvent(new DateTime(2016, 12 - 1, 16, 19, 0, 0)));
        ePlus1.setHeader(h1new);
        ePlus2.setHeader(h1new);

        List<ListItem> newItemsBottom = new ArrayList<>();
        newItemsBottom.add(h1new);
        newItemsBottom.add(ePlus1);
        newItemsBottom.add(ePlus2);

        // ACT
        mUnderTest.mergeNewItems(newItemsBottom, BOTTOM);

        // ASSERT
        verify(mAdapterNotifier, times(1)).notifyNewItemsAdded(Arrays.asList(ePlus1, ePlus2), 3);
        assertSameElementsInProvider(Arrays.<ListItem>asList(h1orig, e1, e2, ePlus1, ePlus2));
        assertHeaderForItem(1, h1orig);
        assertHeaderForItem(2, h1orig);
        assertHeaderForItem(3, h1orig);
        assertHeaderForItem(4, h1orig);
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