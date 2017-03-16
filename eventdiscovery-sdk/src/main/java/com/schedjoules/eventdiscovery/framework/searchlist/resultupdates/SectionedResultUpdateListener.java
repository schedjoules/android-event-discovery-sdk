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
 * Listener interface for components that handle {@link ResultUpdate} marked with section numbers.
 *
 * @author Gabor Keszthelyi
 */
public interface SectionedResultUpdateListener<T>
{

    /**
     * Called when a {@link SearchModule} with the given <code>sectionNumber</code> requests the given update to its section.
     */
    void onUpdate(int sectionNumber, ResultUpdate<T> update);
}
