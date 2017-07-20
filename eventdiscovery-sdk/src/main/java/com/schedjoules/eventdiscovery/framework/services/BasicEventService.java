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

package com.schedjoules.eventdiscovery.framework.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.client.eventsdiscovery.queries.EventByUid;
import com.schedjoules.eventdiscovery.framework.model.resultpage.DescriptionIgnored;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.framework.utils.cache.ExpiringLruCache;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.optional.NullSafe;
import org.dmfs.optional.Optional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;


/**
 * {@link EventService} implementation as a {@link Service} that uses {@link ApiService}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicEventService extends Service implements EventService
{
    private static final int CACHE_SIZE = 300;
    private static final int EXPIRY_TIME_MS = 900_000; // 15 min

    private final ExpiringLruCache<String, Event> mEventCache = new ExpiringLruCache<>(CACHE_SIZE, EXPIRY_TIME_MS);

    private FutureServiceConnection<ApiService> mApiServiceConnection;


    public static void start(Context context)
    {
        context.startService(new Intent(context, BasicEventService.class));
    }


    public static void stop(Context context)
    {
        context.stopService(new Intent(context, BasicEventService.class));
    }


    @Override
    public final void onCreate()
    {
        super.onCreate();
        mApiServiceConnection = new ApiService.FutureConnection(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // Start the service non-sticky. We don't need it to be restarted if it's killed. It will be started automatically when any of our activities is started.
        return START_NOT_STICKY;
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
        return new EventServiceBinder(this);
    }


    @Override
    public Event event(String eventUid) throws TimeoutException, InterruptedException, ProtocolError, IOException, ProtocolException, URISyntaxException
    {
        Event cachedEvent = mEventCache.get(eventUid);
        if (cachedEvent != null)
        {
            return cachedEvent;
        }

        Envelope<Event> envelope = mApiServiceConnection.service(5000).apiResponse(new EventByUid(new StringToken(eventUid)));
        if (!envelope.payload().isPresent())
        {
            throw new RuntimeException("Event Envelope response doesn't contain the event payload");
        }
        Event event = envelope.payload().value();
        mEventCache.put(eventUid, event);
        return event;
    }


    @Override
    public Optional<Event> cachedEvent(String uid)
    {
        return new NullSafe<>(mEventCache.get(uid));
    }


    @Override
    public ResultPage<Envelope<Event>> events(ApiQuery<ResultPage<Envelope<Event>>> query) throws TimeoutException, InterruptedException, ProtocolError, IOException, ProtocolException, URISyntaxException
    {
        ResultPage<Envelope<Event>> resultPage = mApiServiceConnection.service(5000).apiResponse(query);
        for (Envelope<Event> envelope : resultPage)
        {
            Optional<Event> eventOpt = envelope.payload();
            if (eventOpt.isPresent())
            {
                Event event = eventOpt.value();
                mEventCache.put(event.uid(), event);
            }
        }
        return new DescriptionIgnored(resultPage);
    }


    private final static class EventServiceBinder extends Binder implements EventService
    {
        private EventService mDelegate;


        public EventServiceBinder(@NonNull EventService delegate)
        {
            mDelegate = delegate;
        }


        @Override
        public ResultPage<Envelope<Event>> events(ApiQuery<ResultPage<Envelope<Event>>> query) throws TimeoutException, InterruptedException, ProtocolError, IOException, ProtocolException, URISyntaxException
        {
            return mDelegate.events(query);
        }


        @Override
        public Event event(String eventUid) throws TimeoutException, InterruptedException, ProtocolError, IOException, ProtocolException, URISyntaxException
        {
            return mDelegate.event(eventUid);
        }


        @Override
        public Optional<Event> cachedEvent(String uid)
        {
            return mDelegate.cachedEvent(uid);
        }
    }
}
