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

package com.schedjoules.eventdiscovery.framework.location.list;

import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.framework.list.BasicAdapterNotifier;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.changes.ReplaceAll;

import java.util.ArrayList;
import java.util.List;


/**
 * The implementation for {@link PlaceSuggestionListItems}.
 *
 * @author Gabor Keszthelyi
 */
public final class PlaceSuggestionListItemsImpl implements PlaceSuggestionListItems
{
    private List<ListItem> mItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;


    @Override
    public void replaceAllItems(List<ListItem> newItems)
    {
        // TODO make the background task return a ListChange
        new ReplaceAll(newItems).apply(mItems, new BasicAdapterNotifier(mAdapter));
    }


    @Override
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
}
