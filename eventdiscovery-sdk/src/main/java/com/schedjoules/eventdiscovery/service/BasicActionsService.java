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
import android.support.v4.util.LruCache;

import com.schedjoules.client.actions.queries.ActionsQuery;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Marten Gajda
 */
public final class BasicActionsService extends Service implements ActionService
{
    private final static int CACHE_SIZE = 200; // links / actions


    private final static class ActionServiceBinder extends Binder implements ActionService
    {
        private ActionService mDelegate;


        public ActionServiceBinder(@NonNull ActionService delegate)
        {
            mDelegate = delegate;
        }


        @Override
        public List<Link> actions(Token eventUid)
        {
            return mDelegate.actions(eventUid);
        }

    }


    public static void start(Context context)
    {
        context.startService(new Intent(context, BasicActionsService.class));
    }


    public static void stop(Context context)
    {
        context.stopService(new Intent(context, BasicActionsService.class));
    }


    private FutureServiceConnection<ApiService> mApiServiceConnection;


    @Override
    public final void onCreate()
    {
        super.onCreate();
        mApiServiceConnection = new ApiService.FutureConnection(this);
    }


    @Override
    public final void onDestroy()
    {
        mApiServiceConnection.disconnect();
        super.onDestroy();
    }


    @Nullable
    @Override
    public final IBinder onBind(Intent intent)
    {
        return new ActionServiceBinder(this);
    }


    @Override
    public List<Link> actions(Token eventUid)
    {
        return mActionCache.get(eventUid.toString());
    }


    private final LruCache<String, List<Link>> mActionCache = new LruCache<String, List<Link>>(CACHE_SIZE)
    {
        @Override
        protected List<Link> create(String key)
        {
            try
            {
                ApiService service = mApiServiceConnection.service(1000);
                Iterator<Link> links = service.apiResponse(new ActionsQuery(key));
                List<Link> result = new ArrayList<>();
                while (links.hasNext())
                {
                    result.add(links.next());
                }
                return result;
            }
            catch (Exception e)
            {
                return null;
            }
        }


        @Override
        protected int sizeOf(String key, List<Link> value)
        {
            return value.size();
        }
    };
}
