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

package com.schedjoules.eventdiscovery.framework.common;

import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;


/**
 * {@link ListItem} for a header that shows the given text.
 *
 * @author Gabor Keszthelyi
 */
public final class TextHeaderItem implements ListItem<TextView>
{

    private final CharSequence mTitle;


    public TextHeaderItem(CharSequence title)
    {
        mTitle = title;
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_header;
    }


    @Override
    public void bindDataTo(TextView view)
    {
        view.setText(mTitle);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        TextHeaderItem that = (TextHeaderItem) o;

        return mTitle.equals(that.mTitle);

    }


    @Override
    public int hashCode()
    {
        return mTitle.hashCode();
    }


    @Override
    public String toString()
    {
        return mTitle.toString();
    }
}
