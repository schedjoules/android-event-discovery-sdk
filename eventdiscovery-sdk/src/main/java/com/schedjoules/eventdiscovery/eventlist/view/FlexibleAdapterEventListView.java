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

package com.schedjoules.eventdiscovery.eventlist.view;

import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsProviderImpl;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * Handles the RecyclerView and the Adapter initialization for the Event list.
 *
 * @author Gabor Keszthelyi
 */
public final class FlexibleAdapterEventListView implements EventListView
{

    private final FlexibleAdapter<IFlexible> mAdapter;


    public FlexibleAdapterEventListView(RecyclerView recyclerView, EdgeReachScrollListener.Listener edgeScrollListener)
    {
        mAdapter = new FlexibleAdapter<>(null);
        mAdapter.setDisplayHeadersAtStartUp(true);
        mAdapter.setStickyHeaders(true);
        recyclerView.setAdapter(mAdapter);

        EdgeReachScrollListener scrollListener = new EdgeReachScrollListener(recyclerView, edgeScrollListener,
                EventListItemsProviderImpl.CLOSE_TO_TOP_OR_BOTTOM_THRESHOLD);
        recyclerView.addOnScrollListener(scrollListener);
    }


    @Override
    public FlexibleAdapter adapter()
    {
        return mAdapter;
    }

}
