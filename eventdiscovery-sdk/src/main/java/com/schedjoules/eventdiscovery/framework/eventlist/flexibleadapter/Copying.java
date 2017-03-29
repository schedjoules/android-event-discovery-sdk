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

package com.schedjoules.eventdiscovery.framework.eventlist.flexibleadapter;

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * {@link FlexibleAdapter} {@link Factory} decorator that copies all items from a previous {@link FlexibleAdapter} and adds them to the newly instantiated
 * {@link FlexibleAdapter}.
 * <p>
 * Warning: This class captures the old adapter with its context, so reference should not be kept.
 *
 * @author Gabor Keszthelyi
 */
public final class Copying implements Factory<FlexibleAdapter<IFlexible>>
{
    private final Factory<FlexibleAdapter<IFlexible>> mDelegate;
    private final FlexibleAdapter<IFlexible> mOldAdapter;


    public Copying(Factory<FlexibleAdapter<IFlexible>> delegate, FlexibleAdapter<IFlexible> oldAdapter)
    {
        mDelegate = delegate;
        mOldAdapter = oldAdapter;
    }


    @Override
    public FlexibleAdapter<IFlexible> create()
    {
        FlexibleAdapter<IFlexible> newAdapter = mDelegate.create();

        List<IFlexible> oldItems = new ArrayList<>(mOldAdapter.getItemCount());
        for (int i = 0; i < mOldAdapter.getItemCount(); i++)
        {
            oldItems.add(mOldAdapter.getItem(i));
        }
        newAdapter.addItems(0, oldItems);

        return newAdapter;
    }
}
