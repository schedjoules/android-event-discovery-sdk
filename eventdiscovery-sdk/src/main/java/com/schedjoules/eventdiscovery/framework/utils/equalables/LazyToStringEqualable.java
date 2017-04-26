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
 * {@link Equalable} that uses the toString() value of the given object, evaluated lazily, for the equality.
 *
 * @author Gabor Keszthelyi
 */
public final class LazyToStringEqualable implements Equalable
{

    private final Lazy<String> mToString;


    public LazyToStringEqualable(final Object object)
    {
        mToString = new SimpleLazy<>(new Factory<String>()
        {
            @Override
            public String create()
            {
                return object.toString();
            }
        });
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof LazyToStringEqualable))
        {
            return false;
        }

        LazyToStringEqualable that = (LazyToStringEqualable) o;

        return mToString.get().equals(that.mToString.get());
    }


    @Override
    public int hashCode()
    {
        return mToString.get().hashCode();
    }
}
