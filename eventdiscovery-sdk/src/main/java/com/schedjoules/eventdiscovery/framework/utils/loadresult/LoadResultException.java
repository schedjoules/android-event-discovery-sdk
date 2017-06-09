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

/**
 * A {@link Exception} signalling that a load failed, with the cause as the original exception throw at the point of failure.
 * Used by {@link LoadResult}.
 *
 * @author Gabor Keszthelyi
 */
public final class LoadResultException extends Exception
{
    public LoadResultException(Throwable cause)
    {
        super("Load failed", cause);
    }
}
