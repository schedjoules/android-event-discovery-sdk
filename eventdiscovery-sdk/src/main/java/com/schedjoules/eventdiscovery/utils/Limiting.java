/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.utils;

import java.util.Iterator;


/**
 * An {@link Iterator} that returns only the first leading elements of another {@link Iterator}.
 *
 * @author Marten Gajda
 */
public final class Limiting<E> implements Iterable<E>
{
    private final Iterable<E> mDelegate;
    private final int mMaxResults;


    public Limiting(Iterable<E> delegate, int maxResults)
    {
        mDelegate = delegate;
        mMaxResults = maxResults;
    }


    @Override
    public Iterator<E> iterator()
    {
        return new LimitingIterator<>(mDelegate.iterator(), mMaxResults);
    }

}
