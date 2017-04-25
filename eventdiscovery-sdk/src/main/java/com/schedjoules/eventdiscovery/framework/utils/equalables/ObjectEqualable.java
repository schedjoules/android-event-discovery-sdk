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


/**
 * {@link Equalable} that simply uses the provided object as a base for equality.
 *
 * @author Gabor Keszthelyi
 */
public final class ObjectEqualable implements Equalable
{
    private final Object mObject;


    public ObjectEqualable(Object object)
    {
        mObject = object;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ObjectEqualable))
        {
            return false;
        }

        ObjectEqualable that = (ObjectEqualable) o;

        return mObject.equals(that.mObject);

    }


    @Override
    public int hashCode()
    {
        return mObject.hashCode();
    }


    @Override
    public String toString()
    {
        return "ObjectEqualable{" +
                "mObject=" + mObject +
                '}';
    }
}
