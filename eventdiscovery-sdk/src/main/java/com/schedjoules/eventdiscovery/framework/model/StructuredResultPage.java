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

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.ResultPage;

import org.dmfs.optional.Optional;

import java.util.Iterator;


/**
 * A {@link ResultPage} that takes the ready properties as constructor parameters.
 *
 * @author Gabor Keszthelyi
 */
public final class StructuredResultPage<T> implements ResultPage<T>
{
    private final Iterable<T> mItems;
    private final Optional<ApiQuery<ResultPage<T>>> mPrevPageQuery;
    private final Optional<ApiQuery<ResultPage<T>>> mNextPageQuery;


    public StructuredResultPage(Iterable<T> items,
                                Optional<ApiQuery<ResultPage<T>>> prevPageQuery,
                                Optional<ApiQuery<ResultPage<T>>> nextPageQuery)
    {
        mItems = items;
        mPrevPageQuery = prevPageQuery;
        mNextPageQuery = nextPageQuery;
    }


    @Override
    public Iterator<T> iterator()
    {
        return mItems.iterator();
    }


    @Override
    public Optional<ApiQuery<ResultPage<T>>> previousPageQuery()
    {
        return mPrevPageQuery;
    }


    @Override
    public Optional<ApiQuery<ResultPage<T>>> nextPageQuery()
    {
        return mNextPageQuery;
    }
}
