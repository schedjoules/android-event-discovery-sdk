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

package com.schedjoules.eventdiscovery.eventdetails;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ActionLoaderMicroFragment;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ShowEventMicroFragment;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.services.ActionService;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.Swiped;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;


/**
 * {@link EventDetails} of an {@link Event}.
 *
 * @author Marten Gajda
 */
public final class BasicEventDetails implements EventDetails
{
    private final Event mEvent;


    public BasicEventDetails(@NonNull Event event)
    {
        mEvent = event;
    }


    @Override
    public void show(@NonNull final Activity activity)
    {
        final OptionalArgument<MicroFragmentHost> host = new OptionalArgument<>(Keys.MICRO_FRAGMENT_HOST, activity);

        if (!host.isPresent())
        {
            throw new UnsupportedOperationException("Opening EventDetails with already loaded Event, from non-host Activity is not implemented.");
        }

        final Timestamp timestamp = new UiTimestamp();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Optional<List<Link>> actions = loadActionsFromCache(new ActionService.FutureConnection(activity));

                MicroFragment microFragment = actions.isPresent() ?
                        new ShowEventMicroFragment(mEvent, actions.value()) : new ActionLoaderMicroFragment(mEvent);

                host.value().execute(activity, new Swiped(new ForwardTransition<>(microFragment, timestamp)));
            }


            private Optional<List<Link>> loadActionsFromCache(FutureServiceConnection<ActionService> actionService)
            {
                try
                {
                    List<Link> actions = actionService.service(40).cachedActions(mEvent.uid());
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
        }).start();
    }

}
