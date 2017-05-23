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

package com.schedjoules.eventdiscovery.framework.utils.loadresult;

import android.os.Parcelable;


/**
 * The result of a loading operation.
 *
 * @author Gabor Keszthelyi
 */
public interface LoadResult<T> extends Parcelable
{
    /**
     * Tells whether the loading was successful, i.e. if there is a result.
     */
    boolean isSuccess();

    /**
     * Returns the success result. Should call {@link #isSuccess()} first.
     *
     * @throws IllegalStateException
     *         if the load was not successful
     */
    T result() throws IllegalStateException;
}
