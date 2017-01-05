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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.activities.MicroFragmentHostActivity;
import com.schedjoules.eventdiscovery.microfragments.eventdetails.ActionLoaderMicroFragment;
import com.schedjoules.eventdiscovery.microfragments.eventdetails.ShowEventMicroFragment;
import com.schedjoules.eventdiscovery.service.ActionService;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import org.dmfs.httpessentials.types.Link;

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
        new Thread(new StartEventDetailsRunnable(activity, mEvent)).start();
    }


    /**
     * A {@link Runnable} that starts the details activity in the background after trying to load any actions from the cache.
     */
    private final static class StartEventDetailsRunnable implements Runnable
    {
        private final Activity mActivity;
        private final Event mEvent;


        private StartEventDetailsRunnable(@NonNull Activity activity, @NonNull Event event)
        {
            mActivity = activity;
            mEvent = event;
        }


        @Override
        public void run()
        {
            Intent intent = new Intent(mActivity, MicroFragmentHostActivity.class);
            Bundle nestedBundle = new Bundle();

            FutureServiceConnection<ActionService> actionService = new ActionService.FutureConnection(mActivity);
            try
            {
                List<Link> actions = actionService.service(40).cachedActions(mEvent.uid());
                // Start the details with the actions from the cache.
                nestedBundle.putParcelable("MicroFragment", new ShowEventMicroFragment(mEvent, actions));
            }
            catch (InterruptedException | TimeoutException | NoSuchElementException e)
            {
                // An error occurred or the actions are not in the cache yet, let the details activity load the actions.
                nestedBundle.putParcelable("MicroFragment", new ActionLoaderMicroFragment(mEvent));
            }
            finally
            {
                if (actionService.isConnected())
                {
                    actionService.disconnect();
                }
            }
            intent.putExtra("com.schedjoules.nestedExtras", nestedBundle);

            mActivity.startActivity(intent);
        }
    }
}
