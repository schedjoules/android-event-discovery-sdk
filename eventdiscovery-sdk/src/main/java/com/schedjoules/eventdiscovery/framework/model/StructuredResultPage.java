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

package com.schedjoules.eventdiscovery.framework.model;

import android.support.annotation.Nullable;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.ResultPage;


/**
 * A {@link ResultPage} that takes the ready properties as constructor parameters.
 *
 * @author Gabor Keszthelyi
 */
public final class StructuredResultPage<T> implements ResultPage<T>
{
    private final Iterable<T> mItems;
    private final boolean mIsFirstPage;
    private final boolean mIsLastPage;
    private final ApiQuery<ResultPage<T>> mPrevPageQuery;
    private final ApiQuery<ResultPage<T>> mNextPageQuery;


    public StructuredResultPage(Iterable<T> items, boolean isFirstPage, boolean isLastPage,
                                @Nullable ApiQuery<ResultPage<T>> prevPageQuery,
                                @Nullable ApiQuery<ResultPage<T>> nextPageQuery)
    {
        mItems = items;
        mIsFirstPage = isFirstPage;
        mIsLastPage = isLastPage;
        mPrevPageQuery = prevPageQuery;
        mNextPageQuery = nextPageQuery;
    }


    @Override
    public Iterable<T> items()
    {
        return mItems;
    }


    @Override
    public boolean isFirstPage()
    {
        return mIsFirstPage;
    }


    @Override
    public boolean isLastPage()
    {
        return mIsLastPage;
    }


    @Override
    public ApiQuery<ResultPage<T>> previousPageQuery() throws IllegalStateException
    {
        if (mPrevPageQuery == null)
        {
            throw new IllegalStateException("No previous page query");
        }
        return mPrevPageQuery;
    }


    @Override
    public ApiQuery<ResultPage<T>> nextPageQuery() throws IllegalStateException
    {
        if (mNextPageQuery == null)
        {
            throw new IllegalStateException("No next page query");
        }
        return mNextPageQuery;
    }
}
