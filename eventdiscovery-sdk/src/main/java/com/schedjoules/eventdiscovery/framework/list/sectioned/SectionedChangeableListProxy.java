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

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.NonNotifyingChangeableList;
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.NonNotifyingListChange;
import com.schedjoules.eventdiscovery.framework.list.changes.notifying.ChangeableListItems;


/**
 * A {@link NonNotifyingChangeableList} that acts as a proxy to the whole sectioned list held by a {@link ChangeableListItems}.
 *
 * @author Gabor Keszthelyi
 */
public final class SectionedChangeableListProxy implements NonNotifyingChangeableList<ListItem>
{
    private final ChangeableListItems<SectionableListItem> mChangeableListItems;
    private final int mSectionNumber;


    public SectionedChangeableListProxy(ChangeableListItems<SectionableListItem> changeableListItems, int sectionNumber)
    {
        mChangeableListItems = changeableListItems;
        mSectionNumber = sectionNumber;
    }


    @Override
    public void apply(NonNotifyingListChange<ListItem> sectionChange)
    {
        mChangeableListItems.apply(new ListSectionChange(mSectionNumber, sectionChange));
    }
}
