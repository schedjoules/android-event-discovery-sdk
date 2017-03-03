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

package com.schedjoules.eventdiscovery.framework.searchlist;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.ListItems;
import com.schedjoules.eventdiscovery.framework.list.changes.notifying.ChangeableListItems;
import com.schedjoules.eventdiscovery.framework.list.sectioned.SectionableListItem;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.SectionedResultUpdateListener;


/**
 * Component for holding and updating most of the state associated with a search list. i.e. the list of the items, the query, the adapter.
 *
 * @author Gabor Keszthelyi
 */
public interface SearchListItems extends ListItems, SearchQueryInputListener,
        SectionedResultUpdateListener<ListItem>, ChangeableListItems<SectionableListItem>
{
}
