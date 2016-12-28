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
import android.text.format.DateUtils;

import org.dmfs.rfc5545.DateTime;


/**
 * Long formatted date, e.g.: Friday, January 6, 2017
 *
 * @author Gabor Keszthelyi
 */
public final class LongDate implements FormattedDateTime
{
    private final DateTime mDateTime;


    public LongDate(DateTime dateTime)
    {
        mDateTime = dateTime;
    }


    @Override
    public CharSequence value(Context context)
    {
        return DateUtils.formatDateTime(context, mDateTime.getTimestamp(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);
    }
}
