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


/**
 * Date and time formatting.
 *
 * @author Gabor Keszthelyi
 */
// TODO Bug: displayed date-times don't refresh automatically when user changes time zone or locale of the phone
// TODO Check android.text.format.DateUtils methods, using them might be better than fix SimpleDateFormat, eg: it might handle 12/24 hours, 'today', duration formatting automatically according to local.
public final class DateTimeFormatter
{
    //    private static final DateFormat SHORT_DATE_TIME = new SimpleDateFormat("d MMM HH:mm",
//            Locale.getDefault());
    private static final DateFormat DAY_AND_MONTH = new SimpleDateFormat("EEE, d MMM",
            Locale.getDefault());
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

//    public static String shortEventDateTimeRange(Context context, Event event)
//    {
//        return DateUtils.formatDateRange(context, event.start().getTimestamp(), event.start().addDuration(event.duration()).getTimestamp(),
//                DateUtils.FORMAT_SHOW_TIME);
//    }

//    public static String shortEventDateTimeRange(Event event)
//    {
//        return shortDateTimeFormat(event.start())
//                + " - " +
//                shortDateTimeFormat(event.start().addDuration(event.duration()));
//    }

//    private static String shortDateTimeFormat(DateTime dateTime)
//    {
//        return SHORT_DATE_TIME.format(dateTime.getTimestamp());
//    }


    public static String smartDayFormat(Context context, DateTime dateTime)
    {
        if (DateUtils.isToday(dateTime.getTimestamp()))
        {
            return context.getString(R.string.schedjoules_today);
        }
        if (DateUtils.isToday(dateTime.getTimestamp() - DateUtils.DAY_IN_MILLIS))
        {
            return context.getString(R.string.schedjoules_tomorrow);
        }
        return DAY_AND_MONTH.format(dateTime.getTimestamp());
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
