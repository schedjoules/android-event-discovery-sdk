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

import android.util.Log;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;

import java.io.IOException;
import java.net.URI;


/**
 * {@link HttpRequestExecutor} decorator that logs request uris to logcat.
 *
 * @author Gabor Keszthelyi
 */
public class RequestUriLogging implements HttpRequestExecutor
{

    private final HttpRequestExecutor mDelegate;
    private final boolean mEnableLog;
    private final String mTag;


    public RequestUriLogging(HttpRequestExecutor delegate, boolean enableLog, String tag)
    {
        mDelegate = delegate;
        mEnableLog = enableLog;
        mTag = tag;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        if (mEnableLog)
        {
            Log.d(mTag, String.format("%s %s", request.method().verb(), uri));
        }
        return mDelegate.execute(uri, request);
    }
}
