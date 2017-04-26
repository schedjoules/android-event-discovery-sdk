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

package com.schedjoules.eventdiscovery.framework.utils.equalables;

import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.SimpleLazy;


/**
 * {@link Equalable} that uses the object created by the provided factory for equality.
 * It creates the object lazily.
 *
 * @author Gabor Keszthelyi
 */
public final class LazyObjectEqualable implements Equalable
{
    private final Lazy<Object> mLazyObject;


    public LazyObjectEqualable(Factory<Object> factory)
    {
        mLazyObject = new SimpleLazy<>(factory);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof LazyObjectEqualable))
        {
            return false;
        }

        LazyObjectEqualable that = (LazyObjectEqualable) o;

        return mLazyObject.get().equals(that.mLazyObject.get());

    }


    @Override
    public int hashCode()
    {
        return mLazyObject.get().hashCode();
    }


    @Override
    public String toString()
    {
        return "LazyObjectEqualable{" +
                "mLazyObject=" + mLazyObject.get() +
                '}';
    }
}
