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

package com.schedjoules.eventdiscovery.framework.location.listitems;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.smart.AbstractSmartListItem;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.NamedPlace;
import com.schedjoules.eventdiscovery.framework.model.Equalable;


/**
 * {@link ListItem} for the place suggestion items on the location picker.
 *
 * @author Gabor Keszthelyi
 */
public final class PlaceSuggestionItem<D extends NamedPlace & Equalable> extends AbstractSmartListItem<D, PlaceSuggestionItemView<D>>
{
    public PlaceSuggestionItem(D namedPlace)
    {
        super(namedPlace, R.layout.schedjoules_list_item_place_suggestion);
    }


    @Override
    protected String toStringLabel()
    {
        return "PlaceSuggestionItem";
    }

}
