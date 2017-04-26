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

import android.support.annotation.NonNull;

import com.schedjoules.eventdiscovery.framework.model.Equalable;


/**
 * {@link Equalable} that evaluates to equal if both given objects are equal to their counterparts.
 *
 * @author Gabor Keszthelyi
 */
public final class Composite implements Equalable
{
    private final Object mObj1;
    private final Object mObj2;


    public Composite(@NonNull Object obj1, @NonNull Object obj2)
    {
        mObj1 = obj1;
        mObj2 = obj2;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Composite and = (Composite) o;

        if (!mObj1.equals(and.mObj1))
        {
            return false;
        }
        return mObj2.equals(and.mObj2);

    }


    @Override
    public int hashCode()
    {
        int result = mObj1.hashCode();
        result = 31 * result + mObj2.hashCode();
        return result;
    }


    @Override
    public String toString()
    {
        return "Composite{" +
                "mObj1=" + mObj1 +
                ", mObj2=" + mObj2 +
                '}';
    }
}
