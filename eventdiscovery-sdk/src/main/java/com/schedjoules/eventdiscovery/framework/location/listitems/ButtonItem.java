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
import com.schedjoules.eventdiscovery.framework.list.smart.Clickable;


/**
 * A {@link ListItem} on the location picker which looks like a button. Use with {@link Clickable} to receive click events.
 *
 * @author Marten Gajda
 */
public final class ButtonItem extends AbstractSmartListItem<CharSequence, MessageItemView>
{

    public ButtonItem(CharSequence text)
    {
        super(text, R.layout.schedjoules_list_item_location_button);
    }


    @Override
    protected String toStringLabel()
    {
        return "ButtonItem";
    }
}
