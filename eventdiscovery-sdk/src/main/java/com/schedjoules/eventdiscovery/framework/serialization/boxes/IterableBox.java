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

import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Function;

import java.util.LinkedList;
import java.util.List;


/**
 * {@link Box} for {@link Iterable}.
 *
 * @author Gabor Keszthelyi
 */
public final class IterableBox<T> implements Box<Iterable<T>>
{
    private final Iterable<Box<T>> mBoxIterable;


    public IterableBox(Iterable<T> iterable, final BoxFactory<T> boxFactory)
    {
        this(new Mapped<T, Box<T>>(iterable, new Function<T, Box<T>>()
        {
            @Override
            public Box<T> apply(T value)
            {
                return boxFactory.create(value);
            }
        }));
    }


    public IterableBox(Iterable<Box<T>> boxIterable)
    {
        mBoxIterable = boxIterable;
    }


    @Override
    public Iterable<T> content()
    {
        return new Mapped<>(mBoxIterable, new Function<Box<T>, T>()
        {
            @Override
            public T apply(Box<T> box)
            {
                return box.content();
            }
        });
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        List<Box<T>> boxList = new LinkedList<>();
        for (Box<T> box : mBoxIterable)
        {
            boxList.add(box);
        }
        dest.writeList(boxList);
    }


    public static final Creator<IterableBox> CREATOR = new Creator<IterableBox>()
    {
        @Override
        public IterableBox createFromParcel(Parcel in)
        {
            List<Box> list = new LinkedList<>();
            in.readList(list, getClass().getClassLoader());
            return new IterableBox(list);
        }


        @Override
        public IterableBox[] newArray(int size)
        {
            return new IterableBox[size];
        }
    };
}
