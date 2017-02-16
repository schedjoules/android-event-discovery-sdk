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
import com.schedjoules.eventdiscovery.framework.list.changes.nonnotifying.NonNotifyingListChange;


/**
 * {@link ResultUpdate} for a search result. It doesn't apply itself if the query has been changed in the meantime.
 *
 * @author Gabor Keszthelyi
 */
public final class SearchResultUpdate<T> implements ResultUpdate<T>
{
    private final NonNotifyingListChange<T> mNonNotifyingListChange;
    private final String mQuery;


    public SearchResultUpdate(NonNotifyingListChange<T> nonNotifyingListChange, String query)
    {
        mNonNotifyingListChange = nonNotifyingListChange;
        mQuery = query;
    }


    @Override
    public void apply(NonNotifyingChangeableList<T> list, String currentQuery)
    {
        if (currentQuery.equals(mQuery))
        {
            list.apply(mNonNotifyingListChange);
        }
    }
}
