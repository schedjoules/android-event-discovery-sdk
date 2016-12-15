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

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.framework.model.location.geolocation.ParcelableGeoLocation;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;


/**
 * {@link Box} for {@link GeoLocation}.
 *
 * @author Gabor Keszthelyi
 */
public final class GeoLocationBox implements Box<GeoLocation>
{
    private final GeoLocation mGeoLocation;


    public GeoLocationBox(GeoLocation geoLocation)
    {
        mGeoLocation = geoLocation;
    }


    @Override
    public GeoLocation content()
    {
        return mGeoLocation;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mGeoLocation instanceof ParcelableGeoLocation
                ? (ParcelableGeoLocation) mGeoLocation : new ParcelableGeoLocation(mGeoLocation), flags);
    }


    public static final Creator<GeoLocationBox> CREATOR = new Creator<GeoLocationBox>()
    {
        @Override
        public GeoLocationBox createFromParcel(Parcel in)
        {
            GeoLocation geoLocation = in.readParcelable(getClass().getClassLoader());
            return new GeoLocationBox(geoLocation);
        }


        @Override
        public GeoLocationBox[] newArray(int size)
        {
            return new GeoLocationBox[size];
        }
    };
}
