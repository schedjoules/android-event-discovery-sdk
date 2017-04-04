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

package com.schedjoules.eventdiscovery.framework.utils.optionals;

import org.dmfs.optional.Optional;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * {@link Optional} for taking out the first element from an {@link Iterator}.
 *
 * @author Gabor Keszthelyi
 */
public final class First<T> implements Optional<T>
{
    private T mValue;


    public First(Iterator<T> iterator)
    {
        mValue = iterator.hasNext() ? iterator.next() : null;
    }


    @Override
    public boolean isPresent()
    {
        return mValue != null;
    }


    @Override
    public T value(T defaultValue)
    {
        return mValue != null ? mValue : defaultValue;
    }


    @Override
    public T value() throws NoSuchElementException
    {
        if (mValue != null)
        {
            return mValue;
        }
        throw new NoSuchElementException("Iterator is empty");
    }
}
