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

package com.schedjoules.eventdiscovery.framework.list.changes;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.framework.list.ListItem;

import java.util.List;


/**
 * {@link ListChange} that updates the list with the new items using {@link DiffUtil}.
 *
 * @author Gabor Keszthelyi
 */
public final class DiffUtilReplaceAll implements ListChange
{
    public final List<ListItem> mNewItems;


    public DiffUtilReplaceAll(List<ListItem> newItems)
    {
        mNewItems = newItems;
    }


    @Override
    public void apply(List<ListItem> oldItems, RecyclerView.Adapter adapter)
    {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new EqualsDiffUtilCallback<>(oldItems, mNewItems));
        oldItems.clear();
        oldItems.addAll(mNewItems);
        diffResult.dispatchUpdatesTo(adapter);
    }


    private static final class EqualsDiffUtilCallback<T> extends DiffUtil.Callback
    {

        private final List<T> mOldItems;
        private final List<T> mNewItems;


        private EqualsDiffUtilCallback(List<T> oldItems, List<T> newItems)
        {
            mOldItems = oldItems;
            mNewItems = newItems;
        }


        @Override
        public int getOldListSize()
        {
            return mOldItems.size();
        }


        @Override
        public int getNewListSize()
        {
            return mNewItems.size();
        }


        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition)
        {
            return mOldItems.get(oldItemPosition).equals(mNewItems.get(newItemPosition));
        }


        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
        {
            return mOldItems.get(oldItemPosition).equals(mNewItems.get(newItemPosition));
        }
    }

}
