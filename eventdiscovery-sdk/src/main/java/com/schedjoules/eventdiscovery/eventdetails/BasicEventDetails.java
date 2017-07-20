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
import com.schedjoules.eventdiscovery.framework.activities.ActivityMicroFragmentHost;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.EventDetailLoaderMicroFragment;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ShowEventMicroFragment;
import com.schedjoules.eventdiscovery.framework.services.ActionService;
import com.schedjoules.eventdiscovery.framework.services.EventService;
import com.schedjoules.eventdiscovery.framework.utils.anims.BottomUp;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
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
        final Timestamp timestamp = new UiTimestamp();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Optional<List<Link>> cachedActions = loadActionsFromCache(activity);
                Optional<Event> cachedEvent = loadEventFromCache(activity);

                MicroFragment microFragment = cachedActions.isPresent() && cachedEvent.isPresent() ?
                        new ShowEventMicroFragment(mEvent, cachedActions.value())
                        :
                        new EventDetailLoaderMicroFragment(mEvent);

                new ActivityMicroFragmentHost(activity).get()
                        .execute(activity, new BottomUp(new ForwardTransition<>(microFragment, timestamp)));
            }

        }).start();
    }


    private Optional<List<Link>> loadActionsFromCache(Activity activity)
    {
        ActionService.FutureConnection actionService = new ActionService.FutureConnection(activity);
        try
        {
            return new Present<>(actionService.service(40).cachedActions(mEvent.uid()));
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


    private Optional<Event> loadEventFromCache(Activity activity)
    {
        EventService.FutureConnection eventService = new EventService.FutureConnection(activity);
        try
        {
            return eventService.service(40).cachedEvent(mEvent.uid());
        }
        catch (InterruptedException | TimeoutException e)
        {
            return Absent.absent();
        }
        finally
        {
            if (eventService.isConnected())
            {
                eventService.disconnect();
            }
        }
    }

}
