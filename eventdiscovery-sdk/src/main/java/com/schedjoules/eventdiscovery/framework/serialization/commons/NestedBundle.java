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

import android.content.Intent;
import android.os.Bundle;

import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.utils.factory.AbstractLazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;


/**
 * Represents the nested {@link Bundle} in an {@link Intent}.
 *
 * @author Gabor Keszthelyi
 */
public final class NestedBundle extends AbstractLazy<Bundle>
{
    public NestedBundle(final Intent intent)
    {
        super(new Factory<Bundle>()
        {
            @Override
            public Bundle create()
            {
                Bundle nestedBundle = intent.getBundleExtra(Keys.NESTED_BUNDLE);
                return nestedBundle == null ? new Bundle() : nestedBundle;
            }
        });
    }

}
