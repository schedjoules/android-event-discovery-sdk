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

package com.schedjoules.eventdiscovery.framework.utils.iterators;

import org.dmfs.iterators.decorators.Filtered;
import org.dmfs.iterators.filters.Skip;

import java.util.Iterator;


/**
 * An {@link Iterator} that skips leading elements of another {@link Iterator}.
 *
 * @author Marten Gajda
 */
public final class Skipping<E> implements Iterable<E>
{
    private final Iterable<E> mDelegate;
    private final int mSkipResults;


    public Skipping(Iterable<E> delegate, int skipResults)
    {
        mDelegate = delegate;
        mSkipResults = skipResults;
    }


    @Override
    public Iterator<E> iterator()
    {
        return new Filtered<>(mDelegate.iterator(), new Skip<E>(mSkipResults));
    }

}
