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

package com.schedjoules.eventdiscovery.framework.utils.iterables;

import org.dmfs.iterators.EmptyIterator;

import java.util.Iterator;


/**
 * Empty {@link Iterable}.
 *
 * @author Gabor Keszthelyi
 */
// TODO use it from newer iterators library
public final class EmptyIterable<E> implements Iterable<E>
{
    private final static EmptyIterable<?> INSTANCE = new EmptyIterable<>();


    public static <T> EmptyIterable<T> instance()
    {
        //noinspection unchecked
        return (EmptyIterable<T>) INSTANCE;
    }


    @Override
    public Iterator<E> iterator()
    {
        return EmptyIterator.instance();
    }
}
