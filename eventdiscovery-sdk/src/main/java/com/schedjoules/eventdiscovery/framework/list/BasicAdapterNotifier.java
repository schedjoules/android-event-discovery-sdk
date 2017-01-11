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

import android.support.v7.widget.RecyclerView;


/**
 * Basic implementation of {@link AdapterNotifier} that simply delegates to the {@link RecyclerView.Adapter}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicAdapterNotifier implements AdapterNotifier
{
    private final RecyclerView.Adapter mAdapter;


    public BasicAdapterNotifier(RecyclerView.Adapter adapter)
    {
        mAdapter = adapter;
    }


    @Override
    public void notifyDataSetChanged()
    {
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void notifyItemChanged(int position)
    {
        mAdapter.notifyItemChanged(position);
    }


    @Override
    public void notifyItemChanged(int position, Object payload)
    {
        mAdapter.notifyItemChanged(position, payload);
    }


    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount)
    {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }


    @Override
    public void notifyItemInserted(int position)
    {
        mAdapter.notifyItemInserted(position);
    }


    @Override
    public void notifyItemMoved(int fromPosition, int toPosition)
    {
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount)
    {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }


    @Override
    public void notifyItemRemoved(int position)
    {
        mAdapter.notifyItemRemoved(position);
    }


    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount)
    {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }
}
