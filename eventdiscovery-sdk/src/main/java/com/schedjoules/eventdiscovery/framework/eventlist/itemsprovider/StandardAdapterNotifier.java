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

package com.schedjoules.eventdiscovery.framework.eventlist.itemsprovider;

import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.framework.adapter.ListItem;

import java.util.List;


/**
 * {@link AdapterNotifier} adapting standard {@link RecyclerView.Adapter}.
 *
 * @author Gabor Keszthelyi
 */
public final class StandardAdapterNotifier implements AdapterNotifier<ListItem>
{
    private final RecyclerView.Adapter mAdapter;


    public StandardAdapterNotifier(RecyclerView.Adapter adapter)
    {
        mAdapter = adapter;
    }


    @Override
    public void notifyInitialItemsAdded(List initialItems)
    {
        // do nothing, the standard adapter doesn't keep a reference of the items, it keeps state correctly on rotation
    }


    @Override
    public void notifyNewItemsAdded(List newItems, int positionStart)
    {
        mAdapter.notifyItemRangeInserted(positionStart, newItems.size());
    }


    @Override
    public void notifyNewItemAdded(ListItem item, int position)
    {
        mAdapter.notifyItemInserted(position);
    }


    @Override
    public void notifyItemsCleared(int totalSize)
    {
        mAdapter.notifyItemRangeRemoved(0, totalSize);
    }


    @Override
    public void notifyItemRemoved(int position)
    {
        mAdapter.notifyItemRemoved(position);
    }
}
