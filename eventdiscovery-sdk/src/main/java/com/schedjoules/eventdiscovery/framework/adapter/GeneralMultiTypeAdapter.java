/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.framework.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A {@link RecyclerView.Adapter} that can handle multiple types of list items in a general way.
 *
 * @author Gabor Keszthelyi
 */
public final class GeneralMultiTypeAdapter extends RecyclerView.Adapter<GeneralMultiTypeAdapter.SimpleViewHolder>
{
    private final ListItemsProvider mItemsProvider;


    public GeneralMultiTypeAdapter(ListItemsProvider listItemsProvider)
    {
        mItemsProvider = listItemsProvider;
    }


    @Override
    public int getItemViewType(int position)
    {
        return mItemsProvider.get(position).layoutResId();
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // viewType = layoutResId here
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new SimpleViewHolder(view);
    }


    @Override
    public int getItemCount()
    {
        return mItemsProvider.itemCount();
    }


    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position)
    {
        //noinspection unchecked
        mItemsProvider.get(position).bindDataTo(holder.itemView);
    }


    static class SimpleViewHolder extends RecyclerView.ViewHolder
    {
        private SimpleViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
