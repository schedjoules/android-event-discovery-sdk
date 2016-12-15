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

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.schedjoules.eventdiscovery.utils.FutureLocalServiceConnection;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.Token;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;


/**
 * @author Marten Gajda
 */
public interface ActionService
{
    /**
     * A {@link FutureServiceConnection} to the {@link ActionService}
     */
    final class FutureConnection implements FutureServiceConnection<ActionService>
    {
        private final FutureLocalServiceConnection<ActionService> mDelegate;


        /**
         * Creates a future connection to the {@link ApiService} using the given context.
         *
         * @param context
         */
        public FutureConnection(Context context)
        {
            mDelegate = new FutureLocalServiceConnection<>(context, new Intent("com.schedjoules.ACTIONS").setPackage(context.getPackageName()));
        }


        @Override
        public boolean isConnected()
        {
            return mDelegate.isConnected();
        }


        @Override
        public ActionService service(long timeout) throws TimeoutException, InterruptedException
        {
            return mDelegate.service(timeout);
        }


        @Override
        public void disconnect()
        {
            mDelegate.disconnect();
        }
    }

    /**
     * Returns the actions for the event with the given UID. If the actions are not in the cache already, this will perform a network request to retrieve them.
     *
     * @param eventUid
     *         A {@link Token} containing the UID of the event.
     *
     * @return A list of action {@link Link}s.
     */
    @WorkerThread
    @NonNull
    List<Link> actions(@NonNull String eventUid) throws TimeoutException, InterruptedException, ProtocolError, IOException, ProtocolException, URISyntaxException;

    /**
     * Returns any actions for this event from the cache. If there are no action in the cachem this returns {@code null}
     *
     * @param eventUid
     *
     * @return A {@link List} of {@link Link}s or {@code null}.
     */
    @NonNull
    List<Link> cachedActions(@NonNull String eventUid) throws NoSuchElementException;

}
