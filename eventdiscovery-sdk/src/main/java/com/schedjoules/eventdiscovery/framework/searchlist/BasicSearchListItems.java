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

package com.schedjoules.eventdiscovery.framework.searchlist;

import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.changes.notifying.ListChange;
import com.schedjoules.eventdiscovery.framework.list.sectioned.SectionableListItem;
import com.schedjoules.eventdiscovery.framework.list.sectioned.SectionedChangeableListProxy;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdate;

import java.util.ArrayList;
import java.util.List;


/**
 * Basic implementation for {@link SearchListItems}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicSearchListItems implements SearchListItems
{
    private final List<SectionableListItem> mItems = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private String mCurrentQuery;


    @Override
    public void apply(ListChange<SectionableListItem> listChange)
    {
        listChange.apply(mItems, mAdapter);
    }


    public void setAdapter(RecyclerView.Adapter adapter)
    {
        mAdapter = adapter;
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


    @Override
    public void onSearchQueryChange(String newQuery)
    {
        mCurrentQuery = newQuery;
    }


    @Override
    public void onUpdate(final int sectionNumber, final ResultUpdate<ListItem> sectionUpdate)
    {
        /*
         * TODO Aggregate updates to guarantee to make single animation and also for optimization.
         * (If any glitch is encountered with list change animations here, this is the first thing worth doing.)
         *
         * Currently individual updates are waited up and fired right after each other, sort of at the same time,
         * to make sure animations don't overlap. (Done by {@link UpdateDelaying}.
         * Even better would be to also aggregate those changes first, so create a copy of the current full list, update it with all changes and then make the DiffUtil animations once.
         * It would require some refactor.
         */
        sectionUpdate.apply(new SectionedChangeableListProxy(this, sectionNumber), mCurrentQuery);
    }
}
