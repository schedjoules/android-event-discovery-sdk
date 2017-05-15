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

package com.schedjoules.eventdiscovery.framework.serialization.boxes;

import android.os.Parcel;

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;

import org.dmfs.rfc5545.DateTime;

import java.util.TimeZone;


/**
 * {@link Box} for {@link DateTime}.
 *
 * @author Gabor Keszthelyi
 */
public final class DateTimeBox implements Box<DateTime>
{

    private final DateTime mDateTime;


    public DateTimeBox(DateTime dateTime)
    {
        mDateTime = dateTime;
    }


    @Override
    public DateTime content()
    {
        return mDateTime;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        TimeZone timeZone = mDateTime.getTimeZone();
        dest.writeString(timeZone == null ? null : timeZone.getID());
        dest.writeLong(mDateTime.getTimestamp());
        dest.writeInt(mDateTime.isAllDay() ? 1 : 0);

    }


    public static final Creator<DateTimeBox> CREATOR = new Creator<DateTimeBox>()
    {
        @Override
        public DateTimeBox createFromParcel(Parcel in)
        {
            String timeZoneId = in.readString();
            long timestamp = in.readLong();
            boolean isAllDay = in.readInt() != 0;

            TimeZone timeZone = timeZoneId == null ? null : TimeZone.getTimeZone(timeZoneId);
            DateTime dateTime = new DateTime(timeZone, timestamp);
            if (isAllDay)
            {
                dateTime = dateTime.toAllDay();
            }
            return new DateTimeBox(dateTime);
        }


        @Override
        public DateTimeBox[] newArray(int size)
        {
            return new DateTimeBox[size];
        }
    };
}
