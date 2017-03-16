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

package com.schedjoules.eventdiscovery.framework.searchlist.resultupdates;

import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;


/**
 * Listener interface for components that handle {@link ResultUpdate}s.
 *
 * @author Gabor Keszthelyi
 */
public interface ResultUpdateListener<T>
{

    /**
     * Called from {@link SearchModule} then it wants to update its section in the list.
     *
     * @param update
     *         and update to the section of the list
     */
    void onUpdate(ResultUpdate<T> update);
}
