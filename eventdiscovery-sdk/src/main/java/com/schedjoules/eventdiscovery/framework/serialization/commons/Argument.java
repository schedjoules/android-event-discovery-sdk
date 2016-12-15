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

package com.schedjoules.eventdiscovery.framework.serialization.commons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.schedjoules.eventdiscovery.framework.serialization.core.Key;
import com.schedjoules.eventdiscovery.framework.utils.factory.AbstractLazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;


/**
 * Represents an argument in a parameterizable container (Bundle or Intent practically).
 *
 * @author Gabor Keszthelyi
 */
public final class Argument<T> extends AbstractLazy<T>
{
    public Argument(final Key<T> key, final Bundle bundle)
    {
        super(new Factory<T>()
        {
            @Override
            public T create()
            {
                return new OptionalArgument<>(key, bundle).value();
            }
        });
    }


    public Argument(Key<T> key, Intent intent)
    {
        this(key, new NestedBundle(intent).get());
    }


    public Argument(Key<T> key, Activity activity)
    {
        this(key, activity.getIntent());
    }
}
