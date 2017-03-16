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
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.ReplaceAll;


/**
 * {@link ResultUpdate} to show the given item regardless of the current query string.
 *
 * @author Gabor Keszthelyi
 */
public final class ForcedShowSingle<T> implements ResultUpdate<T>
{
    private final T mItem;


    public ForcedShowSingle(T item)
    {
        mItem = item;
    }


    @Override
    public void apply(NonNotifyingChangeableList<T> changeableList, String currentQuery)
    {
        changeableList.apply(new ReplaceAll<T>(mItem));
    }
}
