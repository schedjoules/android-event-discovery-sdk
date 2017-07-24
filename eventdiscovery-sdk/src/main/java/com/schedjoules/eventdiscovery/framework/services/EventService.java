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

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.utils.FutureLocalServiceConnection;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.optional.Optional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;


/**
 * Service interface to fetch events from the API.
 *
 * @author Gabor Keszthelyi
 */
public interface EventService
{

    /**
     * Executes an event discovery request to fetch events matching the query from the API.
     * <p>
     * Caches the returned events as a side effect, to be available through {@link #cachedEvent(String)} and {@link #event(String)}.
     *
     * @param query
     *         the event discovery query
     *
     * @return the list of events as a {@link ResultPage}
     */
    @NonNull
    @WorkerThread
    ResultPage<Envelope<Event>> events(ApiQuery<ResultPage<Envelope<Event>>> query) throws TimeoutException, InterruptedException, ProtocolError, IOException, ProtocolException, URISyntaxException;

    /**
     * Returns the {@link Event} for the given UID. It only contacts the API if the Event is not in cache.
     */
    @NonNull
    @WorkerThread
    Event event(String eventUid) throws TimeoutException, InterruptedException, ProtocolError, IOException, ProtocolException, URISyntaxException;

    /**
     * Returns the {@link Event} immediately from cache if available.
     * Can be called from UI thread.
     */
    Optional<Event> cachedEvent(String uid);

    final class FutureConnection implements FutureServiceConnection<EventService>
    {
        private final FutureLocalServiceConnection<EventService> mDelegate;


        public FutureConnection(Context context)
        {
            mDelegate = new FutureLocalServiceConnection<>(context, new Intent(context, BasicEventService.class));
        }


        @Override
        public boolean isConnected()
        {
            return mDelegate.isConnected();
        }


        @Override
        public EventService service(long timeout) throws TimeoutException, InterruptedException
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
