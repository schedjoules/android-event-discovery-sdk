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

package com.schedjoules.eventdiscovery.framework.eventlist.controller;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.eventlist.items.ErrorItem;
import com.schedjoules.eventdiscovery.framework.eventlist.items.LoadingIndicatorItem;
import com.schedjoules.eventdiscovery.framework.eventlist.items.NoMoreEventsItem;
import com.schedjoules.eventdiscovery.framework.list.flexibleadapter.FlexibleItemAdapter;

import java.util.EnumMap;

import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * Represents an event list scrolling direction.
 *
 * @author Gabor Keszthelyi
 */
public enum ScrollDirection
{
    TOP(
            new ErrorItem(),
            new LoadingIndicatorItem(),
            new NoMoreEventsItem(R.string.schedjoules_event_list_no_past_events))

            {
                @Override
                public <T> boolean hasComingPageQuery(EnumMap<ScrollDirection, ResultPage<T>> lastResultPages)
                {
                    return lastResultPages.get(this) != null && !lastResultPages.get(this).isFirstPage();
                }


                @Override
                public <T> ApiQuery<ResultPage<T>> comingPageQuery(EnumMap<ScrollDirection, ResultPage<T>> lastResultPages) throws IllegalStateException
                {
                    return lastResultPages.get(this).previousPageQuery();
                }
            },

    BOTTOM(
            new ErrorItem(),
            new LoadingIndicatorItem(),
            new NoMoreEventsItem(R.string.schedjoules_event_list_no_future_events))

            {
                @Override
                public <T> boolean hasComingPageQuery(EnumMap<ScrollDirection, ResultPage<T>> lastResultPages)
                {
                    return !lastResultPages.get(this).isLastPage();
                }


                @Override
                public <T> ApiQuery<ResultPage<T>> comingPageQuery(EnumMap<ScrollDirection, ResultPage<T>> lastResultPages) throws IllegalStateException
                {
                    return lastResultPages.get(this).nextPageQuery();
                }
            };

    private final IFlexible mErrorItem;
    private final IFlexible mLoadingIndicatorItem;
    private final IFlexible mNoMoreEventsItem;


    ScrollDirection(ErrorItem errorItem, LoadingIndicatorItem loadingIndicatorItem, NoMoreEventsItem noMoreEventsItem)
    {
        mErrorItem = new FlexibleItemAdapter<>(errorItem);
        mLoadingIndicatorItem = new FlexibleItemAdapter<>(loadingIndicatorItem);
        mNoMoreEventsItem = new FlexibleItemAdapter<>(noMoreEventsItem);
    }


    public IFlexible errorItem()
    {
        return mErrorItem;
    }


    public IFlexible loadingIndicatorItem()
    {
        return mLoadingIndicatorItem;
    }


    public IFlexible noMoreEventsItem()
    {
        return mNoMoreEventsItem;
    }


    public abstract <T> boolean hasComingPageQuery(EnumMap<ScrollDirection, ResultPage<T>> lastResultPages);

    public abstract <T> ApiQuery<ResultPage<T>> comingPageQuery(EnumMap<ScrollDirection, ResultPage<T>> lastResultPages) throws IllegalStateException;
}
