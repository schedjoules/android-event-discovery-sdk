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

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.SimpleLazy;

import org.dmfs.optional.Optional;

import java.util.NoSuchElementException;


/**
 * An abstract {@link Optional} that uses a provided factory to create the optional value which will be cached.
 *
 * @author Gabor Keszthelyi
 */
public abstract class AbstractCachingOptional<T> implements Optional<T>
{
    private final Lazy<Optional<T>> mLazyValue;


    protected AbstractCachingOptional(Factory<Optional<T>> factory)
    {
        mLazyValue = new SimpleLazy<>(factory);
    }


    @Override
    public final boolean isPresent()
    {
        return mLazyValue.get().isPresent();
    }


    @Override
    public final T value(T defaultValue)
    {
        return mLazyValue.get().value(defaultValue);
    }


    @Override
    public final T value() throws NoSuchElementException
    {
        return mLazyValue.get().value();
    }
}
