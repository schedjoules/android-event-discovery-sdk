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

package com.schedjoules.eventdiscovery.location.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.model.ParcelableGeoLocation;


/**
 * {@link Parcelable} decorator for {@link GeoPlace}
 *
 * @author Gabor Keszthelyi
 */
public final class ParcelableGeoPlace implements GeoPlace, Parcelable
{
    private final GeoPlace mDelegate;


    public ParcelableGeoPlace(GeoPlace delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public NamedPlace namedPlace()
    {
        return mDelegate.namedPlace();
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
        NamedPlace namedPlace = namedPlace();
        dest.writeString(namedPlace.id());
        dest.writeString(namedPlace.name().toString());
        dest.writeString(namedPlace.extraContext().toString());

        GeoLocation geoLocation = geoLocation();
        dest.writeParcelable(geoLocation instanceof ParcelableGeoLocation ?
                        (Parcelable) geoLocation : new ParcelableGeoLocation(geoLocation)
                , 0);
    }


    public static final Creator<ParcelableGeoPlace> CREATOR = new Creator<ParcelableGeoPlace>()
    {
        @Override
        public ParcelableGeoPlace createFromParcel(Parcel in)
        {
            return new ParcelableGeoPlace(new StructuredGeoPlace(
                    new StructuredNamedPlace(in.readString(), in.readString(), in.readString()),
                    (GeoLocation) in.readParcelable(getClass().getClassLoader())));
        }


        @Override
        public ParcelableGeoPlace[] newArray(int size)
        {
            return new ParcelableGeoPlace[size];
        }
    };
}
