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

package com.schedjoules.eventdiscovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.schedjoules.client.eventsdiscovery.Address;


/**
 * An {@link Address} that can be parcelled.
 *
 * @author Marten Gajda
 */
public final class ParcelableAddress implements Address, Parcelable
{
    private final Address mDelegate;


    public ParcelableAddress(Address delegate)
    {
        this.mDelegate = delegate;
    }


    @Override
    public String street()
    {
        return mDelegate.street();
    }


    @Override
    public String region()
    {
        return mDelegate.region();
    }


    @Override
    public String locality()
    {
        return mDelegate.locality();
    }


    @Override
    public String postCode()
    {
        return mDelegate.postCode();
    }


    @Override
    public String country()
    {
        return mDelegate.country();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(street());
        dest.writeString(region());
        dest.writeString(locality());
        dest.writeString(postCode());
        dest.writeString(country());
    }


    public static final Creator<ParcelableAddress> CREATOR = new Creator<ParcelableAddress>()
    {
        @Override
        public ParcelableAddress createFromParcel(Parcel in)
        {
            return new ParcelableAddress(new UnparcelledAddress(in.readString(), in.readString(), in.readString(), in.readString(), in.readString()));
        }


        @Override
        public ParcelableAddress[] newArray(int size)
        {
            return new ParcelableAddress[size];
        }
    };


    private final static class UnparcelledAddress implements Address
    {
        private final String mStreet;
        private final String mRegion;
        private final String mLocality;
        private final String mPostCode;
        private final String mCountry;


        private UnparcelledAddress(String street, String region, String locality, String postCode, String country)
        {
            mStreet = street;
            mRegion = region;
            mLocality = locality;
            mPostCode = postCode;
            mCountry = country;
        }


        @Override
        public String street()
        {
            return mStreet;
        }


        @Override
        public String region()
        {
            return mRegion;
        }


        @Override
        public String locality()
        {
            return mLocality;
        }


        @Override
        public String postCode()
        {
            return mPostCode;
        }


        @Override
        public String country()
        {
            return mCountry;
        }
    }
}
