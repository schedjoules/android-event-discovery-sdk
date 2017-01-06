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

package com.schedjoules.eventdiscovery.framework.retain;

/**
 * A {@link RetainedObjects} proxy that can be used to treat the first time load and configuration change reload of the Activity the same way transparently. It
 * delegates to either {@link RealRetainedObjects} or {@link EmptyRetainedObjects}.
 *
 * @author Gabor Keszthelyi
 */
public final class Restoring implements RetainedObjects
{
    private RetainedObjects mDelegate;


    public Restoring(Object lastCustomNonConfigurationInstance)
    {
        if (lastCustomNonConfigurationInstance == null)
        {
            mDelegate = new EmptyRetainedObjects();
        }
        else
        {
            mDelegate = (RetainedObjects) lastCustomNonConfigurationInstance;
        }
    }


    @Override
    public <T> T getOr(int index, T defaultValue)
    {
        return mDelegate.getOr(index, defaultValue);
    }


    @Override
    public boolean hasAny()
    {
        return mDelegate.hasAny();
    }
}
