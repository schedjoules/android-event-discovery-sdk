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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.schedjoules.client.insights.InsightsRequest;
import com.schedjoules.client.insights.Step;
import com.schedjoules.client.insights.sessions.Closed;
import com.schedjoules.client.insights.sessions.SimpleSession;
import com.schedjoules.client.insights.steps.Session;
import com.schedjoules.eventdiscovery.utils.AndroidClient;
import com.schedjoules.eventdiscovery.utils.AndroidPlatform;
import com.schedjoules.eventdiscovery.utils.FutureLocalServiceConnection;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.utils.SharedPrefsUserIdentifier;

import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.rfc5545.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Marten Gajda
 */
public final class BasicInsightsService extends Service implements InsightsService
{
    private final static int MAX_DATA_POINTS = 50;
    private final static long COMMIT_PERIOD = 30000; // milliseconds


    private final static class ApiInsightsBinder extends Binder implements InsightsService
    {
        private InsightsService mDelegate;


        public ApiInsightsBinder(@NonNull InsightsService delegate)
        {
            mDelegate = delegate;
        }


        @Override
        public void post(@NonNull Step... steps)
        {
            mDelegate.post(steps);
        }
    }


    public static void start(Context context)
    {
        context.startService(new Intent(context, BasicInsightsService.class));
    }


    public static void stop(Context context)
    {
        context.stopService(new Intent(context, BasicInsightsService.class));
    }


    private final Timer mTimer = new Timer();
    private FutureServiceConnection<ApiService> mApiServiceConnection;
    private final List<Step> mSteps = new ArrayList<>(MAX_DATA_POINTS);
    private com.schedjoules.client.insights.Session mSession;
    private final DateTime mStart = DateTime.now();


    @Override
    public final void onCreate()
    {
        super.onCreate();
        mApiServiceConnection = new FutureLocalServiceConnection<>(this, new Intent("com.schedjoules.API").setPackage(this.getPackageName()));
        post(new Session(new StringToken("start")));
        mSession = new SimpleSession(new SharedPrefsUserIdentifier(this));
        mTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                flushData(false);
            }
        }, COMMIT_PERIOD, COMMIT_PERIOD);
    }


    @Override
    public final void onDestroy()
    {
        mSession = new Closed(mSession);
        post(new Session(new StringToken("stop")));
        flushData(true);
        mApiServiceConnection.disconnect();
        super.onDestroy();
    }


    @Nullable
    @Override
    public final IBinder onBind(Intent intent)
    {
        return new ApiInsightsBinder(this);
    }


    @Override
    public final void post(Step... steps)
    {
        synchronized (mSteps)
        {
            mSteps.addAll(Arrays.asList(steps));
            if (mSteps.size() > MAX_DATA_POINTS)
            {
                flushData(false);
            }
        }
    }


    private void flushData(boolean wait)
    {
        Thread x = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (mSteps)
                {
                    if (mSteps.isEmpty())
                    {
                        return;
                    }
                    try
                    {
                        if (mApiServiceConnection.service(1000)
                                .apiResponse(
                                        new InsightsRequest(
                                                mSession,
                                                new AndroidPlatform(BasicInsightsService.this),
                                                new AndroidClient(BasicInsightsService.this),
                                                mSteps)))
                        {
                            mSteps.clear();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });
        x.start();
        if (wait)
        {
            try
            {
                x.join(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

}
