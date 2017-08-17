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

package com.schedjoules.eventdiscovery.framework.http;

import android.os.SystemClock;
import android.util.Log;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * {@link HttpRequestExecutor} decorator that logs request uris and round trip times to logcat.
 *
 * @author Gabor Keszthelyi
 */
public final class RequestUriAndTimeLogging implements HttpRequestExecutor
{
    private final HttpRequestExecutor mDelegate;


    public RequestUriAndTimeLogging(HttpRequestExecutor delegate, boolean enableLog, String tag)
    {
        mDelegate = enableLog ? new LoggingExecutor(delegate, tag) : delegate;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return mDelegate.execute(uri, request);
    }


    private static final class LoggingExecutor implements HttpRequestExecutor
    {
        private static AtomicInteger sRequestCounter = new AtomicInteger(1);

        private final HttpRequestExecutor mDelegate;
        private final String mTag;


        private LoggingExecutor(HttpRequestExecutor delegate, String tag)
        {
            mDelegate = delegate;
            mTag = tag;
        }


        @Override
        public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
        {
            int requestId = sRequestCounter.getAndIncrement();
            Log.d(mTag, String.format("(%s) %s %s", requestId, request.method().verb(), uri));
            long start = SystemClock.uptimeMillis();
            T result = mDelegate.execute(uri, request);
            Log.d(mTag, String.format("(%s) Response time: %s ms", requestId, SystemClock.uptimeMillis() - start));

            ByteArrayOutputStream out = new ByteArrayOutputStream(10000);
            request.requestEntity().writeContent(out);
            System.out.println(new String(out.toByteArray()));

            return result;
        }
    }
}
