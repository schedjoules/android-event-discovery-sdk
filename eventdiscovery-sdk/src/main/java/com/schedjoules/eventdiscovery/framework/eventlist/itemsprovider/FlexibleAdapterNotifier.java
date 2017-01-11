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

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * {@link AdapterNotifier} adapting {@link FlexibleAdapter}.
 *
 * @author Gabor Keszthelyi
 */
public final class FlexibleAdapterNotifier implements AdapterNotifier<IFlexible>
{
    private final FlexibleAdapter mFlexibleAdapter;


    public FlexibleAdapterNotifier(FlexibleAdapter flexibleAdapter)
    {
        mFlexibleAdapter = flexibleAdapter;
    }


    @Override
    public void notifyInitialItemsAdded(List initialItems)
    {
        //noinspection unchecked
        mFlexibleAdapter.addItems(0, initialItems);
    }


    @Override
    public void notifyNewItemsAdded(List newItems, int positionStart)
    {
        //noinspection unchecked
        mFlexibleAdapter.addItems(positionStart, newItems);
    }


    @Override
    public void notifyNewItemAdded(IFlexible item, int position)
    {
        //noinspection unchecked
        mFlexibleAdapter.addItem(position, item);
    }


    @Override
    public void notifyItemsCleared(int totalSize)
    {
        mFlexibleAdapter.removeRange(0, totalSize);
    }


    @Override
    public void notifyItemRemoved(int position)
    {
        mFlexibleAdapter.removeItem(position);
    }
}
