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

package com.schedjoules.eventdiscovery.framework.datetime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.schedjoules.client.eventsdiscovery.Event;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;


/**
 * Formatted start and end time, e.g.: "Jan 20, 11:00 - 13:00" or "Jan 20, 20:00 - Jan 21, 03:00" when start and end is not on the same local day.
 *
 * @author Marten Gajda
 */
public final class DateTimeRange implements FormattedDateTime
{
    private final DateTime mStartTime;
    private final Duration mDuration;


    public DateTimeRange(DateTime startTime, Duration duration)
    {
        mStartTime = startTime;
        mDuration = duration;
    }


    public DateTimeRange(Event event)
    {
        this(event.start(), event.duration());
    }


    @NonNull
    @Override
    public CharSequence value(@NonNull Context context)
    {
        if (mDuration == null)
        {
            // duration is unknown, return just the date and time
            return new LongDateAndTimeNoYear(mStartTime).value(context);
        }

        int format = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY;
        if (mStartTime.getYear() == DateTime.nowAndHere().getYear())
        {
            // don't show year if it's this year
            format |= DateUtils.FORMAT_NO_YEAR;
        }

        return DateUtils.formatDateRange(context, mStartTime.getTimestamp(), mStartTime.addDuration(mDuration).getTimestamp(), format);

    }

}
