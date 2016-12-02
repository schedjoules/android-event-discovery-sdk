/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.framework.retain;

/**
 * {@link RetainedObjects} that simply uses an Object array to store the objects in the order of insertion through the
 * constructor.
 *
 * @author Gabor Keszthelyi
 */
public final class RealRetainedObjects implements RetainedObjects
{
    private final Object[] mRetainedObjects;


    public RealRetainedObjects(Object... objectsToRetain)
    {
        mRetainedObjects = objectsToRetain;
    }


    @Override
    public <T> T getOr(int index, T defaultValue)
    {
        return (T) mRetainedObjects[index];
    }

}
