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

import android.app.Activity;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.ListItemSelectionAction;
import com.schedjoules.eventdiscovery.framework.location.PlaceSuggestionModule;


/**
 * Factory for creating {@link SearchModule} for a search list.
 * <p>
 * Note: The reason why the {@link ListItemSelectionAction} is passed in to the {@link SearchModule} to be executed by it, instead of handling it generally
 * outside of the {@link SearchModule}, is that it may need to make extra calls when the user clicks, to fetch the actual data represented by the item. (Example
 * is {@link PlaceSuggestionModule})
 *
 * @author Gabor Keszthelyi
 */
public interface SearchModuleFactory<ITEM_DATA>
{
    /**
     * Creates a new instance of a {@link SearchModule}.
     *
     * @param activity
     *         can be used to initiate connections/resources that the {@link SearchModule} may need
     * @param updateListener
     *         the {@link SearchModule} can use this to send update requests about its section in the list.
     * @param itemSelectionAction
     *         should be executed by the {@link SearchModule} when the user selects an item
     *
     * @return new instance of {@link SearchModule}
     */
    SearchModule create(Activity activity, ResultUpdateListener<ListItem> updateListener, ListItemSelectionAction<ITEM_DATA> itemSelectionAction);
}
