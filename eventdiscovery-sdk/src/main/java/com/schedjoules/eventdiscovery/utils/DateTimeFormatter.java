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

package com.schedjoules.eventdiscovery.utils;

import android.content.Context;
import android.text.format.DateUtils;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_ABBREV_ALL;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;


/**
 * Date and time formatting.
 *
 * @author Gabor Keszthelyi
 */
public final class DateTimeFormatter
{
    private static final DateFormat LONG_DATE_FORMAT = new SimpleDateFormat("EEEE, dd MMMM yyyy",
            Locale.getDefault());
    private static final DateFormat MEDIUM_LONG_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy",
            Locale.getDefault());
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm",
            Locale.getDefault());


    public static String shortEventStartTime(Context context, Event event)
    {
        return DateUtils.formatDateTime(context, event.start().getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
    }


    public static String longEventStartDateTime(Context context, Event event)
    {
        return DateUtils.formatDateTime(context, event.start().getTimestamp(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);
    }


    // TODO test if isToday works correctly with the local time provided
    public static String smartDayFormat(Context context, DateTime localDay)
    {
        long timestamp = localDay.getTimestamp();
        if (DateUtils.isToday(timestamp) || DateUtils.isToday(timestamp - DAY_IN_MILLIS))
        {
            return DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), DAY_IN_MILLIS).toString();
        }
        else
        {
            return DateUtils.formatDateTime(context, timestamp,
                    FORMAT_SHOW_WEEKDAY | FORMAT_SHOW_DATE | FORMAT_ABBREV_ALL);
        }
    }


    public static String longDateFormat(DateTime dateTime)
    {
        return LONG_DATE_FORMAT.format(dateTime.getTimestamp());
    }


    public static String mediumLongDateFormat(DateTime dateTime)
    {
        return MEDIUM_LONG_DATE_FORMAT.format(dateTime.getTimestamp());
    }


    // TODO Can there be events with zero duration?
    public static String longEventTimeFormat(Context context, Event event)
    {
        return String.format(Locale.getDefault(), "%s - %s (%s)",
                TIME_FORMAT.format(event.start().getTimestamp()),
                TIME_FORMAT.format(event.start().addDuration(event.duration()).getTimestamp()),
                durationText(context, event.duration()));

    }


    // TODO If we keep it like this, unit test it
    private static String durationText(Context context, Duration duration)
    {
        StringBuilder sb = new StringBuilder();
        int hours = duration.getHours();
        if (hours != 0)
        {
            sb.append(context.getResources()
                    .getQuantityString(R.plurals.schedjoules_hours, hours, hours));
        }
        int minutes = duration.getMinutes();
        if (minutes != 0)
        {
            if (hours != 0)
            {
                sb.append(" ");
            }
            sb.append(context.getResources()
                    .getQuantityString(R.plurals.schedjoules_minutes, minutes, minutes));
        }
        return sb.toString();
    }

}
