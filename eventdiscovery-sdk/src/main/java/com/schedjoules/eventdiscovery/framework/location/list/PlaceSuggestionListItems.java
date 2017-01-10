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

package com.schedjoules.eventdiscovery.framework.location.list;

import com.schedjoules.eventdiscovery.framework.adapter.ListItem;
import com.schedjoules.eventdiscovery.framework.adapter.ListItems;
import com.schedjoules.eventdiscovery.framework.adapter.notifier.AdapterNotifier;

import java.util.List;


/**
 * Holds the list items for the place suggestions.
 *
 * @author Gabor Keszthelyi
 */
public interface PlaceSuggestionListItems extends ListItems
{

    void replaceAllItems(List<ListItem> newItems);

    // TODO ListItems impls and GeneralMultitypeAdapter have circular reference, that's why setter needed here. Think if there is better way.
    void setAdapterNotifier(AdapterNotifier adapterNotifier);

}
