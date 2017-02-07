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

package com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying;

import android.support.annotation.MainThread;

import java.util.List;


/**
 * A component holding a mutable {@link List} that can be updated by applying a received {@link NonNotifyingListChange}.
 *
 * @author Gabor Keszthelyi
 */
public interface NonNotifyingChangeableList<T>
{

    /**
     * Applies the give {@link NonNotifyingListChange} to the underlying mutable List.
     */
    @MainThread
    void apply(NonNotifyingListChange<T> change);
}
