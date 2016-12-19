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

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.eventlist.itemsprovider.ScrollDirection;

import static com.schedjoules.eventdiscovery.eventlist.itemsprovider.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.eventlist.itemsprovider.ScrollDirection.TOP;


/**
 * A {@link RecyclerView.OnScrollListener} that detects when the user has scrolled close to the bottom or top of the
 * list.
 *
 * @author Gabor Keszthelyi
 */
// TODO Would simply tracking when {@link RecyclerView#onBindViewHolder} is called for position close to size enough as well?
public final class EdgeReachScrollListener extends RecyclerView.OnScrollListener
{

    private final RecyclerView mRecyclerView;
    private final Listener mListener;
    private final int mCloseToTopOrBottomThreshold;


    public EdgeReachScrollListener(RecyclerView recyclerView, Listener listener, int closeToTopOrBottomThreshold)
    {
        mRecyclerView = recyclerView;
        mListener = listener;
        mCloseToTopOrBottomThreshold = closeToTopOrBottomThreshold;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState)
    {
        int totalItemCount = mRecyclerView.getAdapter().getItemCount();
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        boolean isCloseToBottom = lastVisibleItemPosition > totalItemCount - mCloseToTopOrBottomThreshold;
        if (isCloseToBottom)
        {
            mListener.onScrolledCloseToEdge(BOTTOM);
        }

        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        boolean isCloseToTop = firstVisibleItemPosition < mCloseToTopOrBottomThreshold;
        if (isCloseToTop)
        {
            mListener.onScrolledCloseToEdge(TOP);
        }
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        // dy could be used to assess direction, but this method is only called when the scroll finished. TODO try it though
    }


    public interface Listener
    {
        void onScrolledCloseToEdge(ScrollDirection scrollDirection);
    }

}