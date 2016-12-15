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

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;


/**
 * {@link Box} for a {@link Parcelable}.
 *
 * @author Gabor Keszthelyi
 */
public final class ParcelableBox<T extends Parcelable> implements Box<T>
{
    private final T mParcelable;


    public ParcelableBox(T parcelable)
    {
        mParcelable = parcelable;
    }


    @Override
    public T content()
    {
        return mParcelable;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mParcelable, flags);
    }


    public static final Creator<ParcelableBox> CREATOR = new Creator<ParcelableBox>()
    {
        @Override
        public ParcelableBox createFromParcel(Parcel in)
        {
            return new ParcelableBox<>(in.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public ParcelableBox[] newArray(int size)
        {
            return new ParcelableBox[size];
        }
    };
}
