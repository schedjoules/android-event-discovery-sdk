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
import android.os.Bundle;

import com.schedjoules.eventdiscovery.framework.serialization.commons.FluentBundle;
import com.schedjoules.eventdiscovery.framework.serialization.commons.FluentMutable;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;


/**
 * {@link FluentMutable} for updating a context state {@link Bundle}.
 *
 * @author Gabor Keszthelyi
 */
public final class FluentContextState implements FluentMutable
{
    private final Context mContext;


    public FluentContextState(Context context)
    {
        mContext = context;
    }


    @Override
    public <T> FluentMutable put(Key<T> key, Box<T> box)
    {
        return new FluentBundle(new ContextState(mContext).get()).put(key, box);
    }
}
