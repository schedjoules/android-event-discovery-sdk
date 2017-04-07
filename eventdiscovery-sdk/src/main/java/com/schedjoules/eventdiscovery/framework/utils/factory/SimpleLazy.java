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
 * Simple, non-thread safe implementation of {@link Lazy}.
 *
 * @author Gabor Keszthelyi
 */
public final class SimpleLazy<T> implements Lazy<T>
{
    private final Factory<T> mFactory;

    private T mValue;


    public SimpleLazy(Factory<T> factory)
    {
        mFactory = factory;
    }


    @Override
    public T get()
    {
        if (mValue == null)
        {
            mValue = mFactory.create();
        }
        return mValue;
    }
}
