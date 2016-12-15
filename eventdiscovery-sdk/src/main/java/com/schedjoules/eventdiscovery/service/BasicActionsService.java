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
import android.util.LruCache;

import com.schedjoules.client.actions.queries.ActionsQuery;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.Link;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;


/**
 * A basic implementation of an {@link ActionService} that caches the results.
 *
 * @author Marten Gajda
 */
public final class BasicActionsService extends Service implements ActionService
{
    private final static int CACHE_SIZE = 200; // actions


    private final static class ActionServiceBinder extends Binder implements ActionService
    {
        private ActionService mDelegate;


        public ActionServiceBinder(@NonNull ActionService delegate)
        {
            mDelegate = delegate;
        }


        @NonNull
        @Override
        public List<Link> actions(@NonNull String eventUid) throws InterruptedException, ProtocolError, IOException, TimeoutException, URISyntaxException, ProtocolException
        {
            return mDelegate.actions(eventUid);
        }


        @NonNull
        @Override
        public List<Link> cachedActions(@NonNull String eventUid)
        {
            return mDelegate.cachedActions(eventUid);
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


    @NonNull
    @Override
    public List<Link> actions(@NonNull String eventUid) throws TimeoutException, InterruptedException, ProtocolError, IOException, ProtocolException, URISyntaxException
    {
        List<Link> actions = mActionCache.get(eventUid);
        if (actions != null)
        {
            return actions;
        }
        Iterator<Link> links = mApiServiceConnection.service(1000).apiResponse(new ActionsQuery(eventUid));
        List<Link> result = new ArrayList<>(16);
        while (links.hasNext())
        {
            result.add(links.next());
        }
        mActionCache.put(eventUid, result);
        return result;
    }


    @NonNull
    @Override
    public List<Link> cachedActions(@NonNull String eventUid) throws NoSuchElementException
    {
        List<Link> actionLinks = mActionCache.get(eventUid);
        if (actionLinks == null)
        {
            throw new NoSuchElementException(String.format("Element with UID %s not found.", eventUid));
        }
        return actionLinks;
    }


    private final LruCache<String, List<Link>> mActionCache = new LruCache<String, List<Link>>(CACHE_SIZE)
    {
        @Override
        protected int sizeOf(String key, List<Link> value)
        {
            return value.size();
        }
    };
}
