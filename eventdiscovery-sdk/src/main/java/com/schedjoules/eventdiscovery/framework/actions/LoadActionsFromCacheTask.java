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

package com.schedjoules.eventdiscovery.framework.actions;

import android.os.AsyncTask;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTask;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.framework.services.ActionService;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;


/**
 * {@link AsyncTask} to load the Actions (list of {@link Link}s) for an {@link Event} from the cache.
 *
 * @author Gabor Keszthelyi
 */
public final class LoadActionsFromCacheTask extends SafeAsyncTask<Event, FutureServiceConnection<ActionService>, Void, Optional<List<Link>>>
{

    public LoadActionsFromCacheTask(Event event, SafeAsyncTaskCallback<Event, Optional<List<Link>>> callback)
    {
        super(event, callback);
    }


    @Override
    protected Optional<List<Link>> doInBackgroundWithException(Event event, FutureServiceConnection<ActionService>... actionServices) throws Exception
    {
        FutureServiceConnection<ActionService> actionService = actionServices[0];
        try
        {
            List<Link> actions = actionService.service(40).cachedActions(event.uid());
            return new Present<>(actions);
        }
        catch (InterruptedException | TimeoutException | NoSuchElementException e)
        {
            return Absent.absent();
        }
        finally
        {
            if (actionService.isConnected())
            {
                actionService.disconnect();
            }
        }
    }
}
