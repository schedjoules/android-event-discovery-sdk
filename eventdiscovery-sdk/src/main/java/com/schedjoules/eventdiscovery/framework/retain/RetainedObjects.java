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

import android.support.v4.app.FragmentActivity;


/**
 * Holder for the objects retained over configuration changes.
 * <p>
 * (Aimed to reduce boilerplate around usage of {@link FragmentActivity#onRetainCustomNonConfigurationInstance()})
 *
 * @author Gabor Keszthelyi
 */
public interface RetainedObjects
{

    /**
     * Gets the retained object for the given index if there is one, or returns the provided default value otherwise.
     *
     * @param index
     *         the index of the retained object
     * @param defaultValue
     *         the object instance to return if there is no retained object
     * @param <T>
     *         type of the object
     *
     * @return the retained object if there is one, defaultValue otherwise
     */
    <T> T getOr(int index, T defaultValue);

    /**
     * Tells whether there is any retained objects.
     * <p>
     * Can be used in onCreate() to determine if Activity is being recreated after configuration change, in which case
     * the result is true, or being recreated by system because it had been killed while being idle, in which case the
     * result is false.
     */
    boolean hasAny();
}
