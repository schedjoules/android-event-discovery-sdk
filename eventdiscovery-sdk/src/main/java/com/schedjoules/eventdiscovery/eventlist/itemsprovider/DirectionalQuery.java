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
import com.schedjoules.client.eventsdiscovery.ResultPage;

import java.util.Map;


/**
 * Convenience, scrolling direction aware wrapper around {@link ResultPage} to get the next page query.
 *
 * @author Gabor Keszthelyi
 */
public final class DirectionalQuery<E>
{
    private final ScrollDirection mDirection;
    private final Map<ScrollDirection, ResultPage<E>> mResultPages;


    public DirectionalQuery(Map<ScrollDirection, ResultPage<E>> resultPages, ScrollDirection direction)
    {
        mResultPages = resultPages;
        mDirection = direction;
    }


    public boolean hasQuery()
    {
        ResultPage<E> resultPage = mResultPages.get(mDirection);
        if (resultPage == null)
        {
            return false;
        }

        switch (mDirection)
        {
            case TOP:
                return !resultPage.isFirstPage();
            case BOTTOM:
                return !resultPage.isLastPage();
            default:
                throw new IllegalArgumentException("Direction not handled: " + mDirection);
        }
    }


    public ApiQuery<ResultPage<E>> query()
    {
        ResultPage<E> resultPage = mResultPages.get(mDirection);
        switch (mDirection)
        {
            case TOP:
                return resultPage.previousPageQuery();
            case BOTTOM:
                return resultPage.nextPageQuery();
            default:
                throw new IllegalArgumentException("Direction not handled: " + mDirection);
        }
    }
}
