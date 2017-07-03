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

import com.schedjoules.eventdiscovery.framework.serialization.commons.BoxFactory;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;

import org.dmfs.optional.Optional;


/**
 * {@link Box} for an {@link Optional}.
 *
 * @author Gabor Keszthelyi
 */
public final class OptionalBox<T> implements Box<Optional<T>>
{
    private final Box<Optional<T>> mDelegate;


    public OptionalBox(Optional<T> optValue, BoxFactory<T> boxFactory)
    {
        this(optValue.isPresent() ? new PresentBox<>(boxFactory.create(optValue.value())) : new AbsentBox<T>());
    }


    private OptionalBox(Box<Optional<T>> delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public Optional<T> content()
    {
        return mDelegate.content();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mDelegate, flags);
    }


    public static final Creator<Box<Optional>> CREATOR = new Creator<Box<Optional>>()
    {
        @Override
        public Box<Optional> createFromParcel(Parcel in)
        {
            return new OptionalBox((Box<Optional>) in.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public Box<Optional>[] newArray(int size)
        {
            return new OptionalBox[size];
        }
    };
}
