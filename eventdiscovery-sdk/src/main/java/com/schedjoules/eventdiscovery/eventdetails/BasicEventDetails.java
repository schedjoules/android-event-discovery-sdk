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
import com.schedjoules.eventdiscovery.framework.actions.LoadActionsFromCacheTask;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskResult;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ActionLoaderMicroFragment;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ShowEventMicroFragment;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.services.ActionService;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.Swiped;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.Optional;

import java.util.List;


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

        new LoadActionsFromCacheTask(mEvent, new SafeAsyncTaskCallback<Event, Optional<List<Link>>>()
        {
            @Override
            public void onTaskFinish(SafeAsyncTaskResult<Optional<List<Link>>> result, Event event)
            {
                Optional<List<Link>> actions = safeResult(result);

                MicroFragment microFragment = actions.isPresent() ?
                        new ShowEventMicroFragment(mEvent, actions.value()) : new ActionLoaderMicroFragment(mEvent);

                // TODO This couldn't be called from background thread because ForwardTransition's TimeStamp has to be created on main thread.
                // Should we create TimeStamp beforehand to be able to fire the transtion without calling back to main?
                host.value().execute(activity, new Swiped(new ForwardTransition<>(microFragment)));
            }


            private Optional<List<Link>> safeResult(SafeAsyncTaskResult<Optional<List<Link>>> result)
            {
                try
                {
                    return result.value();
                }
                catch (Exception e)
                {
                    // Should not happen
                    throw new RuntimeException(e);
                }
            }

        }).execute(new ActionService.FutureConnection(activity));
    }

}
