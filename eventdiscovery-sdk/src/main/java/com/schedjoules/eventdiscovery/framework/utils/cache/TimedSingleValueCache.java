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

package com.schedjoules.eventdiscovery.framework.utils.cache;

import com.schedjoules.eventdiscovery.framework.model.Equalable;

import org.dmfs.rfc5545.Duration;

import java.util.concurrent.TimeUnit;


/**
 * {@link Cache} that holds a single value for a specified expiration time.
 *
 * @author Gabor Keszthelyi
 */
public final class TimedSingleValueCache<K extends Equalable, V> implements Cache<K, V>
{
    private final long mExpiryTime;

    private long mLastPutTime;

    private K mKey;
    private V mValue;


    public TimedSingleValueCache(long expiryTime, TimeUnit timeUnit)
    {
        mExpiryTime = timeUnit.toMillis(expiryTime);
    }


    public TimedSingleValueCache(Duration expiryDuration)
    {
        mExpiryTime = expiryDuration.toMillis();
    }


    @Override
    public V get(K key)
    {
        if (System.currentTimeMillis() - mLastPutTime > mExpiryTime)
        {
            clear();
            return null;
        }

        if (mKey != null && mKey.equals(key))
        {
            return mValue;
        }
        else
        {
            return null;
        }
    }


    @Override
    public void put(K key, V value)
    {
        mKey = key;
        mValue = value;
        mLastPutTime = System.currentTimeMillis();
    }


    @Override
    public void clear()
    {
        mKey = null;
        mValue = null;
    }
}
