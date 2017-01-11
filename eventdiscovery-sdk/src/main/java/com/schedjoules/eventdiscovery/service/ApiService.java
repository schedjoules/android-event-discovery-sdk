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

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.eventdiscovery.framework.utils.FutureLocalServiceConnection;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;


/**
 * The interface of the services that provides a proxy to the server API.
 *
 * @author Marten Gajda
 */
public interface ApiService
{
    @NonNull
    @WorkerThread
    <T> T apiResponse(@NonNull ApiQuery<T> query) throws URISyntaxException, ProtocolError, ProtocolException, IOException;


    /**
     * A {@link FutureServiceConnection} to the {@link ApiService}
     */
    final class FutureConnection implements FutureServiceConnection<ApiService>
    {
        private final FutureLocalServiceConnection<ApiService> mDelegate;


        /**
         * Creates a future connection to the {@link ApiService} using the given context.
         *
         * @param context
         */
        public FutureConnection(Context context)
        {
            mDelegate = new FutureLocalServiceConnection<>(context, new Intent("com.schedjoules.API").setPackage(context.getPackageName()));
        }


        @Override
        public boolean isConnected()
        {
            return mDelegate.isConnected();
        }


        @Override
        public ApiService service(long timeout) throws TimeoutException, InterruptedException
        {
            return mDelegate.service(timeout);
        }


        @Override
        public void disconnect()
        {
            mDelegate.disconnect();
        }
    }
}
