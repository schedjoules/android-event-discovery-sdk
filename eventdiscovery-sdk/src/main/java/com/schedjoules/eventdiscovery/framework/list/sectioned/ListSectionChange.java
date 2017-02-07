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
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.NonNotifyingListChange;
import com.schedjoules.eventdiscovery.framework.list.changes.notifying.DiffUtilReplaceAll;
import com.schedjoules.eventdiscovery.framework.list.changes.notifying.ListChange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A {@link ListChange} applying a {@link NonNotifyingListChange} representing a change to a section of a list, to the whole list.
 *
 * @author Gabor Keszthelyi
 */
public final class ListSectionChange implements ListChange<SectionableListItem>
{
    private final int mSectionNumber;
    private final NonNotifyingListChange<ListItem> mSectionChange;


    public ListSectionChange(int sectionNumber, NonNotifyingListChange<ListItem> sectionChange)
    {
        mSectionNumber = sectionNumber;
        mSectionChange = sectionChange;
    }


    @Override
    public void apply(List<SectionableListItem> currentItems, RecyclerView.Adapter adapter)
    {
        // Copy the current list to make it an updated version that can be passed in to DiffUtil in the end:
        List<SectionableListItem> newAllItems = new ArrayList<>(currentItems);
        Iterator<SectionableListItem> iterator = newAllItems.iterator();

        // This will be the list for the items currently in the section
        // (they have to extracted from their Sectionable wrappers so that the mSectionChange can be applied to them.)
        List<ListItem> sectionItems = new ArrayList<>();

        int sectionInsertionIndex = -1;
        int i = 0;
        while (iterator.hasNext())
        {
            SectionableListItem nextItem = iterator.next();
            int nextSectionNumber = nextItem.sectionNumber();
            if (sectionInsertionIndex == -1 && (nextSectionNumber == mSectionNumber || nextSectionNumber > mSectionNumber))
            {
                sectionInsertionIndex = i;
            }
            if (nextSectionNumber == mSectionNumber)
            {
                sectionItems.add(nextItem.item());
                iterator.remove();
            }
            if (nextSectionNumber > mSectionNumber)
            {
                break;
            }
            i++;
        }

        if (sectionInsertionIndex == -1)
        {
            sectionInsertionIndex = currentItems.size();
        }

        // Applying the change to the section items:
        mSectionChange.apply(sectionItems);

        // Wrapping changed section items back to Sectionable:
        List<SectionableListItem> sectionableSectionItems = new ArrayList<>();
        for (ListItem sectionItem : sectionItems)
        {
            sectionableSectionItems.add(new BasicSectionableListItem(sectionItem, mSectionNumber));
        }

        // Inserting back the changed section items into the whole list:
        newAllItems.addAll(sectionInsertionIndex, sectionableSectionItems);

        // DiffUtilReplaceAll will take care up updating and animating the list:
        new DiffUtilReplaceAll<>(newAllItems).apply(currentItems, adapter);
    }

}
