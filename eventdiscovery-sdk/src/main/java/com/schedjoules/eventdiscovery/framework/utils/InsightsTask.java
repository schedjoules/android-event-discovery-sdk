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

package com.schedjoules.eventdiscovery.framework.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.schedjoules.client.insights.Step;
import com.schedjoules.eventdiscovery.framework.services.BasicInsightsService;
import com.schedjoules.eventdiscovery.framework.services.InsightsService;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeoutException;


/**
 * @author Marten Gajda
 */
public final class InsightsTask extends AsyncTask<Step, Void, Void>
{
    private final WeakReference<Context> mContextReference;


    public InsightsTask(Context context)
    {
        mContextReference = new WeakReference<>(context);
    }


    @Override
    protected Void doInBackground(Step... params)
    {
        Context context = mContextReference.get();
        if (context == null)
        {
            // TODO: better throw?
            return null;
        }

        FutureServiceConnection<InsightsService> connection = new FutureLocalServiceConnection<>(context, new Intent(context, BasicInsightsService.class));
        try
        {
            connection.service(1000).post(params);
        }
        catch (TimeoutException | InterruptedException e)
        {
            // ignore
            // TODO: better throw?
        }
        finally
        {
            if (connection.isConnected())
            {
                connection.disconnect();
            }
        }
        return null;
    }
}
