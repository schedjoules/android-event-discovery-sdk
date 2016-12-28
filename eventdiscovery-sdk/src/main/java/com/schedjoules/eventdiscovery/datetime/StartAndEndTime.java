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

import com.schedjoules.client.eventsdiscovery.Event;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;


/**
 * Formatted start and end time, e.g.: 11:00 - 13:00
 *
 * @author Gabor Keszthelyi
 */
public final class StartAndEndTime implements FormattedDateTime
{
    private final DateTime mStartTime;
    private final Duration mDuration;


    public StartAndEndTime(DateTime startTime, Duration duration)
    {
        mStartTime = startTime;
        mDuration = duration;
    }


    public StartAndEndTime(Event event)
    {
        this(event.start(), event.duration());
    }


    @NonNull
    @Override
    public CharSequence value(@NonNull Context context)
    {
        return DateUtils.formatDateRange(context,
                mStartTime.getTimestamp(), mStartTime.addDuration(mDuration).getTimestamp(),
                DateUtils.FORMAT_SHOW_TIME);
    }

}
