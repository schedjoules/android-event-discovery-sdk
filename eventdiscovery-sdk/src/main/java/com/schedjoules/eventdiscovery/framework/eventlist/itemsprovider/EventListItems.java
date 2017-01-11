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

package com.schedjoules.eventdiscovery.framework.eventlist.itemsprovider;

import com.schedjoules.eventdiscovery.framework.adapter.ListItem;
import com.schedjoules.eventdiscovery.framework.adapter.ListItemsProvider;

import java.util.List;


/**
 * Component holding the list items for the event list, responsible for updating, merging (date headers) items, and notifying adapter for all changes.
 *
 * @author Gabor Keszthelyi
 */
public interface EventListItems extends ListItemsProvider
{
    void setAdapterNotifier(AdapterNotifier adapterNotifier);

    void mergeNewItems(List<ListItem> newItems, ScrollDirection direction);

    void addSpecialItemPost(ListItem specialItem, ScrollDirection direction);

    void addSpecialItemNow(ListItem specialItem, ScrollDirection direction);

    void removeSpecialItem(ListItem specialItem, ScrollDirection direction);

    boolean isTodayShown();

    boolean isEmpty();

    void clear();
}
