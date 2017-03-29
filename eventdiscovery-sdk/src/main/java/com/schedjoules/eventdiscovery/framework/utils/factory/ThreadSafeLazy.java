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

package com.schedjoules.eventdiscovery.framework.utils.factory;

/**
 * Thread-safe implementation for {@link Lazy} using a provided {@link Factory}.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/Double-checked_locking">Double-checked locking</a>.
 *
 * @author Gabor Keszthelyi
 */
public final class ThreadSafeLazy<T> implements Lazy<T>
{
    private final Factory<T> mFactory;

    private Wrapper<T> mWrapper;


    public ThreadSafeLazy(Factory<T> factory)
    {
        mFactory = factory;
    }


    @Override
    public T get()
    {
        Wrapper<T> tempWrapper = mWrapper;

        if (tempWrapper == null)
        {
            synchronized (this)
            {
                if (mWrapper == null)
                {
                    mWrapper = new Wrapper<T>(mFactory.create());
                }
                tempWrapper = mWrapper;
            }
        }
        return tempWrapper.mValue;
    }


    private static class Wrapper<T>
    {
        private final T mValue;


        private Wrapper(T value)
        {
            mValue = value;
        }
    }

}
