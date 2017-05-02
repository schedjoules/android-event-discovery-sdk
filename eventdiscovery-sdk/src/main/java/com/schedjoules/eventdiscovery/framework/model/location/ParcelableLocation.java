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

package com.schedjoules.eventdiscovery.framework.model.location;

import android.os.Parcel;
import android.os.Parcelable;

import com.schedjoules.client.eventsdiscovery.Address;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.Location;
import com.schedjoules.eventdiscovery.framework.model.ParcelableAddress;
import com.schedjoules.eventdiscovery.framework.model.location.geolocation.ParcelableGeoLocation;

import java.util.TimeZone;


/**
 * A {@link Location} that can be parcelled.
 *
 * @author Marten Gajda
 */
public final class ParcelableLocation implements Location, Parcelable
{
    public static final Creator<ParcelableLocation> CREATOR = new Creator<ParcelableLocation>()
    {
        @Override
        public ParcelableLocation createFromParcel(Parcel in)
        {
            ClassLoader loader = getClass().getClassLoader();
            String name = in.readString();
            String rel = in.readString();
            String timeZone = in.readString();
            ParcelableAddress address = in.readParcelable(loader);
            ParcelableGeoLocation geolocation = in.readParcelable(loader);
            return new ParcelableLocation(new UnparcelledLocation(name, rel, timeZone, address, geolocation));
        }


        @Override
        public ParcelableLocation[] newArray(int size)
        {
            return new ParcelableLocation[size];
        }
    };
    private final Location mDelegate;


    public ParcelableLocation(Location delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public String name()
    {
        return mDelegate.name();
    }


    @Override
    public String rel()
    {
        return mDelegate.rel();
    }


    @Override
    public TimeZone timeZone()
    {
        return mDelegate.timeZone();
    }


    @Override
    public Address address()
    {
        return mDelegate.address();
    }


    @Override
    public GeoLocation geoLocation()
    {
        return mDelegate.geoLocation();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name());
        dest.writeString(rel());
        TimeZone timeZone = timeZone();
        dest.writeString(timeZone == null ? null : timeZone.getID());
        Address address = address();
        dest.writeParcelable(address == null ? null : new ParcelableAddress(address), flags);
        GeoLocation geoLocation = geoLocation();
        dest.writeParcelable(geoLocation == null ? null : new ParcelableGeoLocation(geoLocation), flags);
    }


    private final static class UnparcelledLocation implements Location
    {
        private final String mName;
        private final String mRel;
        private final String mTimeZone;
        private final Address mAddress;
        private final GeoLocation mGeoLocation;


        private UnparcelledLocation(String name, String rel, String timeZone, Address address, GeoLocation geoLocation)
        {
            mName = name;
            mRel = name;
            mTimeZone = name;
            mAddress = address;
            mGeoLocation = geoLocation;
        }


        @Override
        public String name()
        {
            return mName;
        }


        @Override
        public String rel()
        {
            return mRel;
        }


        @Override
        public TimeZone timeZone()
        {
            return mTimeZone == null ? null : TimeZone.getTimeZone(mTimeZone);
        }


        @Override
        public Address address()
        {
            return mAddress;
        }


        @Override
        public GeoLocation geoLocation()
        {
            return mGeoLocation;
        }
    }

}
