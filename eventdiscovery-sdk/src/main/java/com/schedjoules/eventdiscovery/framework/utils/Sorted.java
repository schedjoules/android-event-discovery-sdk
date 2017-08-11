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

package com.schedjoules.eventdiscovery.framework.utils;

import android.support.annotation.NonNull;

import org.dmfs.iterators.Function;
import org.dmfs.iterators.decorators.Mapped;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;


/**
 * An {@link Iterable} decorator which returns the elements of the delegate in sorted order.
 *
 * @author Marten Gajda
 */
public final class Sorted<T> implements Iterable<T>
{
    private final Iterable<T> mDelegate;
    private final Comparator<T> mComparator;


    public Sorted(Iterable<T> delegate, Comparator<T> comparator)
    {
        mDelegate = delegate;
        mComparator = comparator;
    }


    @Override
    public Iterator<T> iterator()
    {
        TreeSet<ComparableAdapter<T>> treeSet = new TreeSet<ComparableAdapter<T>>();
        for (T element : mDelegate)
        {
            treeSet.add(new ComparableAdapter<>(mComparator, element));
        }
        return new Mapped<>(treeSet.iterator(), new Function<ComparableAdapter<T>, T>()
        {
            @Override
            public T apply(ComparableAdapter<T> argument)
            {
                return argument.mElement;
            }
        });
    }


    private final class ComparableAdapter<T> implements Comparable<ComparableAdapter<T>>
    {
        private final Comparator<T> mComparator;
        private final T mElement;


        private ComparableAdapter(Comparator<T> comparator, T element)
        {
            mComparator = comparator;
            mElement = element;
        }


        @Override
        public int compareTo(@NonNull ComparableAdapter<T> o)
        {
            return mComparator.compare(mElement, o.mElement);
        }
    }
}
