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

package com.schedjoules.eventdiscovery.service;

import android.os.AsyncTask;

import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;


/**
 * A simple {@link ServiceJobQueue} to execute {@link ServiceJob}s in the background.
 *
 * @author Marten Gajda
 */
public final class SimpleServiceJobQueue<S> implements ServiceJobQueue<S>
{
    private final FutureServiceConnection<S> mFutureConnection;
    private final Executor mExecutor;


    public SimpleServiceJobQueue(FutureServiceConnection<S> futureConnection)
    {
        this(futureConnection, AsyncTask.SERIAL_EXECUTOR);
    }


    public SimpleServiceJobQueue(FutureServiceConnection<S> futureConnection, Executor executor)
    {
        mFutureConnection = futureConnection;
        mExecutor = executor;
    }


    @Override
    public void post(final ServiceJob<S> job, final int executionTimeout)
    {
        mExecutor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    job.execute(mFutureConnection.service(executionTimeout));
                    mFutureConnection.disconnect();
                }
                catch (TimeoutException | InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    job.onTimeOut();
                }
            }
        });
    }
}
