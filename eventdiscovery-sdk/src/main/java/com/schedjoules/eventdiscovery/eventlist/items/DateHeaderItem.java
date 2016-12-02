/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.eventlist.items;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.adapter.ListItem;
import com.schedjoules.eventdiscovery.framework.adapter.flexibleadapter.AbstractFlexibleHeader;
import com.schedjoules.eventdiscovery.utils.DateTimeFormatter;

import org.dmfs.rfc5545.DateTime;


/**
 * Even list item representing a date header.
 *
 * @author Gabor Keszthelyi
 */
public final class DateHeaderItem extends AbstractFlexibleHeader<DateHeaderItemView> implements ListItem<DateHeaderItemView>
{
    private final DateTime mDateTime;


    public DateHeaderItem(DateTime dateTime)
    {
        mDateTime = dateTime;
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_date_header;
    }


    @Override
    public void bindDataTo(DateHeaderItemView view)
    {
        view.setDateText(DateTimeFormatter.smartDayFormat(view.getContext(), mDateTime));
    }


    @Override
    public String toString()
    {
        return mDateTime.toString();
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

        DateHeaderItem that = (DateHeaderItem) o;

        return mDateTime.equals(that.mDateTime);

    }


    @Override
    public int hashCode()
    {
        return mDateTime.hashCode();
    }
}
