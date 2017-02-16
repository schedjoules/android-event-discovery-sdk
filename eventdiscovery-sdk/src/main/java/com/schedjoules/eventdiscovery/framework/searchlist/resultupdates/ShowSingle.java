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
 * {@link ResultUpdate} to show the given item only, if the current query string still matches.
 *
 * @author Gabor Keszthelyi
 */
public final class ShowSingle<T> implements ResultUpdate<T>
{
    private final T mItem;
    private final String mQuery;


    public ShowSingle(T item, String query)
    {
        mItem = item;
        mQuery = query;
    }


    @Override
    public void apply(NonNotifyingChangeableList<T> changeableList, String currentQuery)
    {
        new SearchResultUpdate<>(new ReplaceAll<>(mItem), mQuery).apply(changeableList, currentQuery);
    }
}
