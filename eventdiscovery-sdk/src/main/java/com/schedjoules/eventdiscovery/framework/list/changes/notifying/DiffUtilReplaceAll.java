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

package com.schedjoules.eventdiscovery.framework.list.changes.notifying;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.framework.list.ListItem;

import java.util.List;


/**
 * {@link ListChange} that updates the list with the new items using {@link DiffUtil}.
 *
 * @author Gabor Keszthelyi
 */
public final class DiffUtilReplaceAll<LI extends ListItem> implements ListChange<LI>
{
    private final List<LI> mNewItems;


    public DiffUtilReplaceAll(List<LI> newItems)
    {
        mNewItems = newItems;
    }


    @Override
    public void apply(List<LI> currentItems, RecyclerView.Adapter adapter)
    {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new EqualsDiffUtilCallback(currentItems, mNewItems));
        currentItems.clear();
        // TODO Can the order of the items change as a result to DiffUtil update? If yes, then we keep an incorrect order here in currentItems.
        // There was some strange re-ordering animation, so this may be the cause, not sure.
        currentItems.addAll(mNewItems);
        diffResult.dispatchUpdatesTo(adapter);
    }


    private static final class EqualsDiffUtilCallback<LI extends ListItem> extends DiffUtil.Callback
    {

        private final List<LI> mOldItems;
        private final List<LI> mNewItems;


        private EqualsDiffUtilCallback(List<LI> oldItems, List<LI> newItems)
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
            return mOldItems.get(oldItemPosition).id().equals(mNewItems.get(newItemPosition).id());
        }


        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
        {
            // Distinguishing between items' content is not supported currently
            return true;
        }
    }

}
