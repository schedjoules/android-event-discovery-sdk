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

import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.NonNotifyingChangeableList;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;


/**
 * Represents and update to a section of a search list. {@link SearchModule}s can create these and pass on to {@link ResultUpdateListener}.
 *
 * @author Gabor Keszthelyi
 */
public interface ResultUpdate<T>
{

    /**
     * Applies the changes of this update to the given list.
     * <p>
     * Depending on the update it can check if currentQuery matches the query this update belongs to and discard if it doesn't.
     *
     * @param changeableList
     *         the changeable list representing a section of the whole list
     * @param currentQuery
     *         the current (last) query entered by the user
     */
    void apply(NonNotifyingChangeableList<T> changeableList, String currentQuery);
}