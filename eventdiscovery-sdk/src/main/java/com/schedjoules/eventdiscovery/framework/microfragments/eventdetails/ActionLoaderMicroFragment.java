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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventDetailsContentLoadingActionsBinding;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.views.EventHeaderView;
import com.schedjoules.eventdiscovery.framework.model.ParcelableEvent;
import com.schedjoules.eventdiscovery.framework.services.ActionService;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJob;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJobQueue;
import com.schedjoules.eventdiscovery.framework.utils.SimpleServiceJobQueue;
import com.schedjoules.eventdiscovery.framework.utils.anims.Revealed;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.Link;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;


/**
 * A {@link MicroFragment} that loads the actions of a given event.
 */
public final class ActionLoaderMicroFragment implements MicroFragment<Event>
{
    public final static Creator<ActionLoaderMicroFragment> CREATOR = new Creator<ActionLoaderMicroFragment>()
    {
        @Override
        public ActionLoaderMicroFragment createFromParcel(Parcel source)
        {
            return new ActionLoaderMicroFragment((Event) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public ActionLoaderMicroFragment[] newArray(int size)
        {
            return new ActionLoaderMicroFragment[size];
        }
    };
    private final Event mEvent;


    public ActionLoaderMicroFragment(Event event)
    {
        mEvent = event;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        return "Loading …";
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, MicroFragmentHost host)
    {
        return new LoaderFragment();
    }


    @NonNull
    @Override
    public Event parameter()
    {
        return mEvent;
    }


    @Override
    public boolean skipOnBack()
    {
        return true;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mEvent instanceof Parcelable ? (Parcelable) mEvent : new ParcelableEvent(mEvent), flags);
    }


    public final static class LoaderFragment extends Fragment
    {
        private final Timestamp mTimestamp = new UiTimestamp();
        private ServiceJobQueue<ActionService> mActionServiceJobQueue;
        private Event mEvent;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mActionServiceJobQueue = new SimpleServiceJobQueue<>(new ActionService.FutureConnection(getActivity()));
            mEvent = new FragmentEnvironment<Event>(this).microFragment().parameter();
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            SchedjoulesFragmentEventDetailsContentLoadingActionsBinding views = DataBindingUtil.inflate(inflater,
                    R.layout.schedjoules_fragment_event_details_content_loading_actions, container, false);

            new EventHeaderView(getActivity(), views.schedjoulesDetailsHeader).update(mEvent);

            return views.getRoot();
        }


        @Override
        public void onResume()
        {
            super.onResume();
            mActionServiceJobQueue.post(new ServiceJob<ActionService>()
            {
                @Override
                public void execute(ActionService service)
                {
                    try
                    {
                        List<Link> links = service.actions(mEvent.uid());
                        startTransition(new Revealed(new ForwardTransition(new ShowEventMicroFragment(mEvent, links), mTimestamp)));
                    }
                    catch (TimeoutException | InterruptedException | ProtocolError | IOException | ProtocolException | URISyntaxException | RuntimeException e)
                    {
                        // for some reason we were unable to load the actions, move on without actions.
                        startTransition(new Revealed(new ForwardTransition(new ShowEventMicroFragment(mEvent, Collections.<Link>emptyList()), mTimestamp)));
                    }
                }


                @Override
                public void onTimeOut()
                {
                    startTransition(new Revealed(new ForwardTransition(new ErrorMicroFragment(), mTimestamp)));
                }
            }, 5000);
        }


        @Override
        public void onDestroy()
        {
            mActionServiceJobQueue.disconnect();
            super.onDestroy();
        }


        private void startTransition(FragmentTransition fragmentTransition)
        {
            if (isResumed())
            {
                new FragmentEnvironment<>(this).host().execute(getActivity(), fragmentTransition);
            }
        }
    }
}
