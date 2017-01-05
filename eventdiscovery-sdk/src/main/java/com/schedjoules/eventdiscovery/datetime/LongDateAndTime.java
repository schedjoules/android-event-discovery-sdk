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

package com.schedjoules.eventdiscovery.datetime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import org.dmfs.rfc5545.DateTime;


/**
 * Long date and time format, e.g.: "Friday, December 30, 23:00"; "Friday, December 30, 2017, 23:00"
 *
 * @author Gabor Keszthelyi
 */
public final class LongDateAndTime implements FormattedDateTime
{
    private final DateTime mDateTime;


    public LongDateAndTime(DateTime dateTime)
    {
        mDateTime = dateTime;
    }


    @NonNull
    @Override
    public CharSequence value(@NonNull Context context)
    {
        return DateUtils.formatDateTime(context, mDateTime.getTimestamp(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);
    }
}
