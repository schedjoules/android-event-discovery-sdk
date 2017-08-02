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

package com.schedjoules.eventdiscovery.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.schedjoules.client.AccessToken;
import com.schedjoules.client.Api;
import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.SchedJoulesApi;
import com.schedjoules.client.SchedJoulesApiClient;
import com.schedjoules.eventdiscovery.framework.http.DefaultExecutor;
import com.schedjoules.eventdiscovery.framework.utils.SharedPrefsUserIdentifier;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author Marten Gajda
 */
public abstract class AbstractApiService extends Service
{
    private final ApiFactory mApiFactory;


    public AbstractApiService(@NonNull ApiFactory apiFactory)
    {
        mApiFactory = apiFactory;
    }


    @Nullable
    @Override
    public final IBinder onBind(Intent intent)
    {
        return new ApiServiceBinder(mApiFactory.schedJoulesApi(this));
    }


    /**
     * A factory that creates Api instances.
     */
    public interface ApiFactory
    {
        /**
         * Create a new SchedJoules {@link Api} instance.
         *
         * @param context
         *         A Context.
         *
         * @return
         */
        @NonNull
        Api schedJoulesApi(@NonNull Context context);
    }


    /**
     * A {@link Binder} that gives access to the SchedJoules API.
     */
    private final static class ApiServiceBinder extends Binder implements ApiService
    {

        private final Api mApi;


        public ApiServiceBinder(@NonNull Api api)
        {
            mApi = api;
        }


        @NonNull
        @Override
        public <T> T apiResponse(@NonNull ApiQuery<T> query) throws URISyntaxException, ProtocolError, ProtocolException, IOException
        {
            return query.queryResult(mApi);
        }
    }


    /**
     * The default SchedJoules {@link Api} to be used by most partners.
     *
     * @author Marten Gajda
     */
    public static final class DefaultApi implements Api
    {
        private final Api mDelegate;


        public DefaultApi(Context context, AccessToken accessToken)
        {
            this(context, accessToken, new DefaultExecutor(context));
        }


        public DefaultApi(Context context, AccessToken accessToken, HttpRequestExecutor executor)
        {
            mDelegate = new SchedJoulesApi(
                    new SchedJoulesApiClient(accessToken),
                    executor,
                    new SharedPrefsUserIdentifier(context));
        }


        @Override
        public <T> T queryResult(URI uri, HttpRequest<T> httpRequest) throws ProtocolException, ProtocolError, IOException
        {
            return mDelegate.queryResult(uri, httpRequest);
        }
    }
}
