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

import java.util.Collections;
import java.util.List;


/**
 * {@link NonNotifyingListChange} that replaces the list's content with the received new items.
 *
 * @author Gabor Keszthelyi
 */
public final class ReplaceAll<T> implements NonNotifyingListChange<T>
{
    private final List<T> mNewItems;


    public ReplaceAll(List<T> newItems)
    {
        mNewItems = newItems;
    }


    public ReplaceAll(T item)
    {
        this(Collections.<T>singletonList(item));
    }


    @Override
    public void apply(List<T> currentItems)
    {
        currentItems.clear();
        currentItems.addAll(mNewItems);
    }
}
