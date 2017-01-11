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

package com.schedjoules.eventdiscovery.framework.list.changes;

import com.schedjoules.eventdiscovery.framework.list.AdapterNotifier;
import com.schedjoules.eventdiscovery.framework.list.ListChange;
import com.schedjoules.eventdiscovery.framework.list.ListItem;

import java.util.List;


/**
 * {@link ListChange} that replaces all items in the list, i.e. clears the list and adds the new Items.
 *
 * @author Gabor Keszthelyi
 */
public final class ReplaceAll implements ListChange
{
    public final List<ListItem> mNewItems;


    public ReplaceAll(List<ListItem> newItems)
    {
        mNewItems = newItems;
    }


    @Override
    public void apply(List<ListItem> items, AdapterNotifier adapterNotifier)
    {
        int sizeBefore = items.size();

        items.clear();
        items.addAll(mNewItems);

        adapterNotifier.notifyItemRangeRemoved(0, sizeBefore);
        adapterNotifier.notifyItemRangeInserted(0, items.size());
    }
}
