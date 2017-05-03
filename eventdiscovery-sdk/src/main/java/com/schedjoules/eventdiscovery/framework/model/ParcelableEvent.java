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

package com.schedjoules.eventdiscovery.framework.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.Location;
import com.schedjoules.eventdiscovery.framework.model.location.ParcelableLocation;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.iterators.AbstractConvertedIterator;
import org.dmfs.iterators.ArrayIterator;
import org.dmfs.iterators.ConvertedIterator;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;


/**
 * An {@link Event} that can be parcelled.
 *
 * @author Marten Gajda
 */
public final class ParcelableEvent implements Event, Parcelable
{
    public static final Creator<ParcelableEvent> CREATOR = new Creator<ParcelableEvent>()
    {
        @Override
        public ParcelableEvent createFromParcel(Parcel in)
        {
            ClassLoader loader = getClass().getClassLoader();
            return new ParcelableEvent(
                    new UnparcelledEvent(in.readString(), in.readLong(), in.readString(), in.readInt() == 1, in.readString(), in.readString(),
                            in.readString(), in.readParcelableArray(loader), in.readParcelableArray(loader)));
        }


        @Override
        public ParcelableEvent[] newArray(int size)
        {
            return new ParcelableEvent[size];
        }
    };
    private final Event mDelegate;


    public ParcelableEvent(Event delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public String uid()
    {
        return mDelegate.uid();
    }


    @Override
    public DateTime start()
    {
        return mDelegate.start();
    }


    @Override
    public Duration duration()
    {
        return mDelegate.duration();
    }


    @Override
    public String title()
    {
        return mDelegate.title();
    }


    @Override
    public String description()
    {
        return mDelegate.description();
    }


    @Override
    public Iterable<Location> locations()
    {
        return mDelegate.locations();
    }


    @Override
    public Iterable<Link> links()
    {
        return mDelegate.links();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(uid());
        dest.writeLong(start().getTimestamp());
        dest.writeString(start().getTimeZone().getID());
        dest.writeInt(start().isAllDay() ? 1 : 0);
        dest.writeString(duration().toString());
        dest.writeString(title());
        dest.writeString(description());

        List<ParcelableLocation> locations = new ArrayList<>();
        for (Location location : mDelegate.locations())
        {
            locations.add(new ParcelableLocation(location));
        }

        List<ParcelableLink> links = new ArrayList<>();
        for (Link link : mDelegate.links())
        {
            links.add(new ParcelableLink(link));
        }

        dest.writeParcelableArray(locations.toArray(new ParcelableLocation[locations.size()]), flags);
        dest.writeParcelableArray(links.toArray(new ParcelableLink[links.size()]), flags);
    }


    /**
     * A helper event that can be populated from a parcel.
     */
    private final static class UnparcelledEvent implements Event
    {
        private final String mUid;
        private final long mTimestamp;
        private final String mTimeZoneId;
        private final boolean mAllDay;
        private final String mDuration;
        private final String mTitle;
        private final String mDescription;
        private final Parcelable[] mLocations;
        private final Parcelable[] mLinks;


        private UnparcelledEvent(String uid, long timestamp, String timeZoneId, boolean allDay, String duration, String title, String description, Parcelable[] locations, Parcelable[] links)
        {
            mUid = uid;
            mTimestamp = timestamp;
            mTimeZoneId = timeZoneId;
            mAllDay = allDay;
            mDuration = duration;
            mTitle = title;
            mDescription = description;
            mLocations = locations;
            mLinks = links;
        }


        @Override
        public String uid()
        {
            return mUid;
        }


        @Override
        public DateTime start()
        {
            return mAllDay ? new DateTime(TimeZone.getTimeZone(mTimeZoneId), mTimestamp).toAllDay() : new DateTime(TimeZone.getTimeZone(mTimeZoneId),
                    mTimestamp);
        }


        @Override
        public Duration duration()
        {
            return Duration.parse(mDuration);
        }


        @Override
        public String title()
        {
            return mTitle;
        }


        @Override
        public String description()
        {
            return mDescription;
        }


        @Override
        public Iterable<Location> locations()
        {
            return new Iterable<Location>()
            {
                @Override
                public Iterator<Location> iterator()
                {
                    return new ConvertedIterator<>(new ArrayIterator<>(mLocations), new AbstractConvertedIterator.Converter<Location, Parcelable>()
                    {
                        @Override
                        public Location convert(Parcelable element)
                        {
                            return (Location) element;
                        }
                    });
                }
            };
        }


        @Override
        public Iterable<Link> links()
        {
            return new Iterable<Link>()
            {
                @Override
                public Iterator<Link> iterator()
                {
                    return new ConvertedIterator<>(new ArrayIterator<>(mLinks), new AbstractConvertedIterator.Converter<Link, Parcelable>()
                    {
                        @Override
                        public Link convert(Parcelable element)
                        {
                            return (Link) element;
                        }
                    });
                }
            };
        }
    }
}
