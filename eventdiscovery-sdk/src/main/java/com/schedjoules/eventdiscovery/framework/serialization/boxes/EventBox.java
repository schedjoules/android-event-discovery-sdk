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
import android.os.Parcelable;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.model.ParcelableEvent;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;


/**
 * {@link Box} for {@link Event}.
 *
 * @author Gabor Keszthelyi
 */
public final class EventBox implements Box<Event>
{
    private final Event mEvent;


    public EventBox(Event event)
    {
        mEvent = event;
    }


    @Override
    public Event content()
    {
        return mEvent;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mEvent instanceof Parcelable ? (Parcelable) mEvent : new ParcelableEvent(mEvent), flags);
    }


    public final static Creator<EventBox> CREATOR = new Creator<EventBox>()
    {
        @Override
        public EventBox createFromParcel(Parcel source)
        {
            Event event = source.readParcelable(getClass().getClassLoader());
            return new EventBox(event);
        }


        @Override
        public EventBox[] newArray(int size)
        {
            return new EventBox[size];
        }
    };
}
