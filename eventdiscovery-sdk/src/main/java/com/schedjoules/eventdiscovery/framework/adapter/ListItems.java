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

package com.schedjoules.eventdiscovery.framework.adapter;

import android.support.v7.widget.RecyclerView;


/**
<<<<<<< HEAD:eventdiscovery-sdk/src/main/java/com/schedjoules/eventdiscovery/framework/adapter/ListItems.java
 * Represents the list items for a {@link RecyclerView}, used by {@link GeneralMultiTypeAdapter}.
=======
 * Provides the list items for {@link GeneralMultiTypeAdapter}. Implementation can handle data loading and binding any sort.
>>>>>>> master:eventdiscovery-sdk/src/main/java/com/schedjoules/eventdiscovery/framework/adapter/ListItemsProvider.java
 *
 * @author Gabor Keszthelyi
 */
public interface ListItems
{
    /**
     * Returns the item for the given position in the list
     */
    ListItem get(int position);

    /**
     * Returns the total number of list items.
     */
    int itemCount();

}
