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

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.locations.StructuredGeoLocation;


/**
 * A {@link GeoLocation} that can be parcelled.
 *
 * @author Marten Gajda
 */
public final class ParcelableGeoLocation implements GeoLocation, Parcelable
{
    public static final Creator<ParcelableGeoLocation> CREATOR = new Creator<ParcelableGeoLocation>()
    {
        @Override
        public ParcelableGeoLocation createFromParcel(Parcel in)
        {
            return new ParcelableGeoLocation(new StructuredGeoLocation(in.readFloat(), in.readFloat()));
        }


        @Override
        public ParcelableGeoLocation[] newArray(int size)
        {
            return new ParcelableGeoLocation[size];
        }
    };
    private final GeoLocation mDelegate;


    public ParcelableGeoLocation(GeoLocation delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public float latitude()
    {
        return mDelegate.latitude();
    }


    @Override
    public float longitude()
    {
        return mDelegate.longitude();
    }


    @Override
    public int hashCode()
    {
        return 31 * (latitude() != +0.0f ? Float.floatToIntBits(latitude()) : 0) + (longitude() != +0.0f ? Float.floatToIntBits(longitude()) : 0);
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof GeoLocation))
        {
            return false;
        }
        return Float.compare(latitude(), ((GeoLocation) obj).latitude()) == 0 && Float.compare(longitude(), ((GeoLocation) obj).longitude()) == 0;

    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeFloat(mDelegate.latitude());
        dest.writeFloat(mDelegate.longitude());
    }
}
