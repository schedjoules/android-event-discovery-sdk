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

import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;


/**
 * {@link Box} for an present {@link Optional} value.
 *
 * @author Gabor Keszthelyi
 */
public final class PresentBox<T> implements Box<Optional<T>>
{
    private final Box<T> mBox;


    public PresentBox(Box<T> box)
    {
        mBox = box;
    }


    @Override
    public Optional<T> content()
    {
        return new Present<>(mBox.content());
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mBox, flags);
    }


    public static final Creator<PresentBox> CREATOR = new Creator<PresentBox>()
    {
        @Override
        public PresentBox createFromParcel(Parcel in)
        {
            return new PresentBox((Box) in.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public PresentBox[] newArray(int size)
        {
            return new PresentBox[size];
        }
    };
}
