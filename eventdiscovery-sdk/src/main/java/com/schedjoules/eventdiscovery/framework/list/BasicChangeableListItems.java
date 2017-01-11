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

package com.schedjoules.eventdiscovery.framework.list;

import java.util.ArrayList;
import java.util.List;


/**
 * Basic implementation of {@link ChangeableListItems}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicChangeableListItems implements ChangeableListItems
{
    private List<ListItem> mItems = new ArrayList<>();

    private AdapterNotifier mAdapterNotifier;


    @Override
    public void apply(ListChange listChange)
    {
        listChange.apply(mItems, mAdapterNotifier);
    }


    @Override
    public void setAdapterNotifier(AdapterNotifier adapterNotifier)
    {
        mAdapterNotifier = adapterNotifier;
    }


    @Override
    public ListItem get(int position)
    {
        return mItems.get(position);
    }


    @Override
    public int itemCount()
    {
        return mItems.size();
    }
}
