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

package com.schedjoules.eventdiscovery.framework.async;

/**
 * Used by {@link DiscardingSafeAsyncTask} to discard (cancel) a task in any of its phases.
 *
 * @author Gabor Keszthelyi
 */
public interface DiscardCheck<TASK_PARAM>
{
    /**
     * Tells whether the {@link DiscardingSafeAsyncTask} with the given task parameters should be discarded.
     */
    boolean shouldDiscard(TASK_PARAM taskParam);
}
