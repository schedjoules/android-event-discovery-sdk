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

package com.schedjoules.eventdiscovery.framework.eventlist.items;

import android.support.annotation.StringRes;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;


/**
 * Represents a message item at the bottom or top of the event list that says no more events found.
 *
 * @author Gabor Keszthelyi
 */
public final class NoMoreEventsItem implements ListItem<TextView>
{
    private final int mText;


    public NoMoreEventsItem(@StringRes int text)
    {
        mText = text;
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_special_text;
    }


    @Override
    public void bindDataTo(TextView view)
    {
        view.setText(mText);
    }
}
