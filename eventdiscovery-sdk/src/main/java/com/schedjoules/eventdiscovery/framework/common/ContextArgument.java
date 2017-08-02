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

package com.schedjoules.eventdiscovery.framework.common;

import android.content.Context;

import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;
import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;


/**
 * Represents an argument 'belonging to a Context scope'.
 *
 * @author Gabor Keszthelyi
 */
public final class ContextArgument<T> implements Lazy<T>
{
    private final Lazy<T> mDelegate;


    public ContextArgument(Key<T> key, Context context)
    {
        mDelegate = new Argument<T>(key, new ContextState(context).get());
    }


    @Override
    public T get()
    {
        return mDelegate.get();
    }

}
