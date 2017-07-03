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

package com.schedjoules.eventdiscovery.testutils;

import com.schedjoules.client.Api;
import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.State;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;


/**
 * @author Gabor Keszthelyi
 */
public final class DummyResultPage implements ResultPage<Envelope<Event>>
{
    public static final URI URI_TOP = URI.create("http://TOP");
    public static final URI URI_BOTTOM = URI.create("http://BOTTOM");

    private final Iterable<Envelope<Event>> mItems;
    private final boolean mIsFirst;
    private final boolean mIsLast;


    public DummyResultPage(Iterable<Envelope<Event>> items)
    {
        this(items, false, false);
    }


    public DummyResultPage(Iterable<Envelope<Event>> items, boolean isFirst, boolean isLast)
    {
        mItems = items;
        mIsFirst = isFirst;
        mIsLast = isLast;
    }


    @Override
    public Optional<ApiQuery<ResultPage<Envelope<Event>>>> previousPageQuery() throws IllegalStateException
    {
        if (mIsFirst)
        {
            throw new IllegalStateException("No previous query");
        }
        return new Present<ApiQuery<ResultPage<Envelope<Event>>>>(new ApiQuery<ResultPage<Envelope<Event>>>()
        {
            @Override
            public ResultPage<Envelope<Event>> queryResult(Api api) throws IOException, URISyntaxException, ProtocolError, ProtocolException
            {
                return api.queryResult(URI_TOP, null);
            }


            @Override
            public State<ApiQuery<ResultPage<Envelope<Event>>>> serializable()
            {
                throw new UnsupportedOperationException();
            }
        });
    }


    @Override
    public Optional<ApiQuery<ResultPage<Envelope<Event>>>> nextPageQuery() throws IllegalStateException
    {
        if (mIsLast)
        {
            throw new IllegalStateException("No next query");
        }
        return new Present<ApiQuery<ResultPage<Envelope<Event>>>>(new ApiQuery<ResultPage<Envelope<Event>>>()
        {
            @Override
            public ResultPage<Envelope<Event>> queryResult(Api api) throws IOException, URISyntaxException, ProtocolError, ProtocolException
            {
                return api.queryResult(URI_BOTTOM, null);
            }


            @Override
            public State<ApiQuery<ResultPage<Envelope<Event>>>> serializable()
            {
                throw new UnsupportedOperationException();
            }
        });
    }


    @Override
    public Iterator<Envelope<Event>> iterator()
    {
        return mItems.iterator();
    }
}
