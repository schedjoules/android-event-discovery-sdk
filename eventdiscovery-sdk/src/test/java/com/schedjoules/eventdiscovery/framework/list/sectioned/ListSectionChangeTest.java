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

package com.schedjoules.eventdiscovery.framework.list.sectioned;

import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.ClearAll;
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.ReplaceAll;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.schedjoules.eventdiscovery.framework.list.sectioned.TestListItem.testItem;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Unit test for {@link ListSectionChange}.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ RecyclerView.Adapter.class })
public final class ListSectionChangeTest
{

    private RecyclerView.Adapter mAdapter;


    @Before
    public void setup()
    {
        mAdapter = PowerMockito.mock(RecyclerView.Adapter.class);
    }


    @Test
    public void testMultipleChanges()
    {
        List<SectionableListItem> currentItems = new ArrayList<>();

        // Adding 2 items
        List<ListItem> newItems = asList(
                testItem(101),
                testItem(102));
        new ListSectionChange(100, new ReplaceAll<>(newItems)).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(100, 101),
                        sectionedItem(100, 102)),
                currentItems);

        // Clearing the items
        new ListSectionChange(100, new ClearAll<ListItem>()).apply(currentItems, mAdapter);
        assertTrue(currentItems.isEmpty());

        // Adding 1 item
        newItems = asList(
                testItem(201));
        new ListSectionChange(200, new ReplaceAll<>(newItems)).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(200, 201)),
                currentItems);

        // Adding 3 items to new top section
        newItems = asList(
                testItem(1510),
                testItem(1520),
                testItem(1530));
        new ListSectionChange(150, new ReplaceAll<>(newItems)).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(150, 1510),
                        sectionedItem(150, 1520),
                        sectionedItem(150, 1530),
                        sectionedItem(200, 201)),
                currentItems);

        // Adding 2 items in the middle
        newItems = asList(
                testItem(171),
                testItem(172));
        new ListSectionChange(170, new ReplaceAll<>(newItems)).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(150, 1510),
                        sectionedItem(150, 1520),
                        sectionedItem(150, 1530),
                        sectionedItem(170, 171),
                        sectionedItem(170, 172),
                        sectionedItem(200, 201)),
                currentItems);

        // Adding 1 item at the bottom
        newItems = asList(
                testItem(301));
        new ListSectionChange(300, new ReplaceAll<>(newItems)).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(150, 1510),
                        sectionedItem(150, 1520),
                        sectionedItem(150, 1530),
                        sectionedItem(170, 171),
                        sectionedItem(170, 172),
                        sectionedItem(200, 201),
                        sectionedItem(300, 301)),
                currentItems);

        // Replacing top section with 1 common element
        newItems = asList(
                testItem(1515),
                testItem(1510),
                testItem(1540));
        new ListSectionChange(150, new ReplaceAll<>(newItems)).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(150, 1515),
                        sectionedItem(150, 1510),
                        sectionedItem(150, 1540),
                        sectionedItem(170, 171),
                        sectionedItem(170, 172),
                        sectionedItem(200, 201),
                        sectionedItem(300, 301)),
                currentItems);

        // Replacing middle section with no common elements
        newItems = asList(
                testItem(173),
                testItem(174),
                testItem(175));
        new ListSectionChange(170, new ReplaceAll<>(newItems)).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(150, 1515),
                        sectionedItem(150, 1510),
                        sectionedItem(150, 1540),
                        sectionedItem(170, 173),
                        sectionedItem(170, 174),
                        sectionedItem(170, 175),
                        sectionedItem(200, 201),
                        sectionedItem(300, 301)),
                currentItems);

        // Replacing bottom section with one extra element
        newItems = asList(
                testItem(302),
                testItem(301));
        new ListSectionChange(300, new ReplaceAll<>(newItems)).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(150, 1515),
                        sectionedItem(150, 1510),
                        sectionedItem(150, 1540),
                        sectionedItem(170, 173),
                        sectionedItem(170, 174),
                        sectionedItem(170, 175),
                        sectionedItem(200, 201),
                        sectionedItem(300, 302),
                        sectionedItem(300, 301)
                ),
                currentItems);

        // Clearing sections:
        new ListSectionChange(200, new ClearAll<ListItem>()).apply(currentItems, mAdapter);
        assertEquals(
                asList(sectionedItem(150, 1515),
                        sectionedItem(150, 1510),
                        sectionedItem(150, 1540),
                        sectionedItem(170, 173),
                        sectionedItem(170, 174),
                        sectionedItem(170, 175),
                        sectionedItem(300, 302),
                        sectionedItem(300, 301)
                ),
                currentItems);
        new ListSectionChange(150, new ClearAll<ListItem>()).apply(currentItems, mAdapter);
        assertEquals(
                asList(
                        sectionedItem(170, 173),
                        sectionedItem(170, 174),
                        sectionedItem(170, 175),
                        sectionedItem(300, 302),
                        sectionedItem(300, 301)
                ),
                currentItems);
        new ListSectionChange(300, new ClearAll<ListItem>()).apply(currentItems, mAdapter);
        assertEquals(
                asList(
                        sectionedItem(170, 173),
                        sectionedItem(170, 174),
                        sectionedItem(170, 175)
                ),
                currentItems);
        new ListSectionChange(170, new ClearAll<ListItem>()).apply(currentItems, mAdapter);
        assertEquals(
                Collections.emptyList(),
                currentItems);
    }


    private SectionableListItem sectionedItem(int sectionNumber, int id)
    {
        return new BasicSectionableListItem(new TestListItem(id), sectionNumber);
    }

}