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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.format.DateUtils;

import com.schedjoules.eventdiscovery.R;

import org.dmfs.rfc5545.DateTime;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_ABBREV_WEEKDAY;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;


/**
 * Creates the following relative and absolute localized date formats:
 * <p>
 * "Today", "Tomorrow", "Mon, Oct 24", and "Sun, Jan 1, 2017" if the year is different than current.
 *
 * @author Gabor Keszthelyi
 */
public final class SmartFormattedDay implements FormattedDateTime
{
    private final DateTime mDateTime;


    public SmartFormattedDay(@NonNull DateTime dateTime)
    {
        mDateTime = dateTime;
    }


    @Override
    public CharSequence value(Context context)
    {
        long timestamp = mDateTime.getTimestamp();

        if (DateUtils.isToday(timestamp))
        {
            return relativeDay(context, timestamp, R.string.schedjoules_today);
        }
        else if (DateUtils.isToday(timestamp - DAY_IN_MILLIS))
        {
            return relativeDay(context, timestamp, R.string.schedjoules_tomorrow);
        }
        else
        {
            return DateUtils.formatDateTime(context, timestamp,
                    FORMAT_SHOW_WEEKDAY | FORMAT_SHOW_DATE | FORMAT_ABBREV_WEEKDAY);
        }
    }


    private CharSequence relativeDay(Context context, long timestamp, @StringRes int dayName)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            return context.getString(dayName);
        }

        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(timestamp, now, DAY_IN_MILLIS).toString();
    }

}
