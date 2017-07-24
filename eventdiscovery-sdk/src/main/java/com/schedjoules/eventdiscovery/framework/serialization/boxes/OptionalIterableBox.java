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
 * {@link Box} for {@link Optional} of {@link Iterable}.
 *
 * @author Gabor Keszthelyi
 */
public final class OptionalIterableBox<T> implements Box<Optional<Iterable<T>>>
{
    private final Box<Optional<Iterable<T>>> mDelegate;


    public OptionalIterableBox(Optional<Iterable<T>> value, BoxFactory<T> boxFactory)
    {
        this(value.isPresent()
                ?
                new PresentBox<>(new IterableBox<>(value.value(), boxFactory))
                :
                new AbsentBox<Iterable<T>>());
    }


    private OptionalIterableBox(Box<Optional<Iterable<T>>> delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public Optional<Iterable<T>> content()
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


    public static final Creator<OptionalIterableBox> CREATOR = new Creator<OptionalIterableBox>()
    {
        @Override
        public OptionalIterableBox createFromParcel(Parcel in)
        {
            Box<Optional<Iterable>> delegate = in.readParcelable(getClass().getClassLoader());
            //noinspection unchecked
            return new OptionalIterableBox(delegate);
        }


        @Override
        public OptionalIterableBox[] newArray(int size)
        {
            return new OptionalIterableBox[size];
        }
    };
}
