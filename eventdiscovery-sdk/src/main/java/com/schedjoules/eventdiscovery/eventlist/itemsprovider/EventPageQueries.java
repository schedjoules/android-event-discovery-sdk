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

package com.schedjoules.eventdiscovery.eventlist.itemsprovider;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;

import java.util.HashMap;
import java.util.Map;


/**
 * Implementation for {@link PageQueries}.
 *
 * @author Gabor Keszthelyi
 */
public final class EventPageQueries implements PageQueries<Envelope<Event>>
{
    private final Map<ScrollDirection, ResultPage<Envelope<Event>>> mLastResultPages = new HashMap<>(2);


    @Override
    public void saveLastResult(ResultPage<Envelope<Event>> resultPage, ScrollDirection direction)
    {
        mLastResultPages.put(direction, resultPage);
    }


    @Override
    public boolean hasComingPage(ScrollDirection direction)
    {
        ResultPage<Envelope<Event>> resultPage = mLastResultPages.get(direction);
        if (resultPage == null)
        {
            return false;
        }

        switch (direction)
        {
            case TOP:
                return !resultPage.isFirstPage();
            case BOTTOM:
                return !resultPage.isLastPage();
            default:
                throw new IllegalArgumentException("Direction not handled: " + direction);
        }
    }


    @Override
    public ApiQuery<ResultPage<Envelope<Event>>> comingPageQuery(ScrollDirection direction) throws IllegalStateException
    {
        ResultPage<Envelope<Event>> resultPage = mLastResultPages.get(direction);
        if (!hasComingPage(direction))
        {
            throw new IllegalStateException("No coming page for direction: " + direction);
        }
        switch (direction)
        {
            case TOP:
                return resultPage.previousPageQuery();
            case BOTTOM:
                return resultPage.nextPageQuery();
            default:
                throw new IllegalArgumentException("Direction not handled: " + direction);
        }
    }


    @Override
    public void clear()
    {
        mLastResultPages.clear();
    }
}
