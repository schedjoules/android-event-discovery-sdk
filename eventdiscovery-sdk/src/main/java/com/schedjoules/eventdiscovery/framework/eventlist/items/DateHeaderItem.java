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

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.datetime.FormattedDateTime;
import com.schedjoules.eventdiscovery.framework.datetime.SmartFormattedDay;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.model.Equalable;

import org.dmfs.rfc5545.DateTime;

import java.util.TimeZone;


/**
 * Even list item representing a date header.
 *
 * @author Gabor Keszthelyi
 */
public final class DateHeaderItem implements ListItem<TextView>
{

    private final DateTime mLocalDay;

    // Should not be used for equals()
    private final FormattedDateTime formattedDay;


    public DateHeaderItem(DateTime dateTime)
    {
        mLocalDay = toLocalDay(dateTime);
        formattedDay = new SmartFormattedDay(dateTime);
    }


    private DateTime toLocalDay(DateTime dateTime)
    {
        return dateTime.shiftTimeZone(TimeZone.getDefault()).toAllDay();
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_header_cards;
    }


    @Override
    public void bindDataTo(TextView view)
    {
        view.setText(formattedDay.value(view.getContext()));
    }


    @NonNull
    @Override
    public Equalable id()
    {
        throw new UnsupportedOperationException("ListItem id is not supported in Event List currently");
    }


    @Override
    public String toString()
    {
        return mLocalDay.toString();
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

        return mLocalDay.equals(that.mLocalDay);

    }


    @Override
    public int hashCode()
    {
        return mLocalDay.hashCode();
    }
}
