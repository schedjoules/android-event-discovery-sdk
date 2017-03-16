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


/**
 * Interface for a simple cache.
 *
 * @author Gabor Keszthelyi
 */
public interface Cache<K extends Equalable, V>
{
    /**
     * Gets the value stored for the given key or null if missing.
     */
    V get(K key);

    /**
     * Stores the given key-value pair in the cache.
     */
    void put(K key, V value);

    /**
     * Clears all entries from the cache.
     */
    void clear();
}
