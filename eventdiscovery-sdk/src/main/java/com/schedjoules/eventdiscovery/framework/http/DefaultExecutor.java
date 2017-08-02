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

import android.content.Context;

import com.schedjoules.eventdiscovery.framework.utils.AppProduct;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.executors.retrying.Retrying;
import org.dmfs.httpessentials.executors.retrying.policies.DefaultRetryPolicy;
import org.dmfs.httpessentials.executors.useragent.Branded;
import org.dmfs.httpessentials.httpurlconnection.HttpUrlConnectionExecutor;
import org.dmfs.httpessentials.httpurlconnection.factories.DefaultHttpUrlConnectionFactory;
import org.dmfs.httpessentials.httpurlconnection.factories.decorators.Finite;

import java.io.IOException;
import java.net.URI;


/**
 * The default {@link HttpRequestExecutor} used by the SDK if no other has been specified.
 *
 * @author Marten Gajda
 */
public final class DefaultExecutor implements HttpRequestExecutor
{
    private final HttpRequestExecutor mDelegate;


    public DefaultExecutor(Context context)
    {
        mDelegate = new Branded(
                new Retrying(
                        new HttpUrlConnectionExecutor(
                                new Finite(
                                        new DefaultHttpUrlConnectionFactory(),
                                        5000, 5000)),
                        new DefaultRetryPolicy(3)),
                new AppProduct(context));
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return mDelegate.execute(uri, request);
    }
}
