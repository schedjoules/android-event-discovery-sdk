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
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.queries.EventByUid;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.services.ActionService;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJob;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJobQueue;
import com.schedjoules.eventdiscovery.framework.utils.SimpleServiceJobQueue;
import com.schedjoules.eventdiscovery.service.ApiService;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.dmfs.android.microfragments.BasicMicroFragmentEnvironment;
import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.transitions.Faded;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.StringToken;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;


/**
 * A {@link MicroFragment} that loads an event given by UID.
 */
public final class EventLoaderMicroFragment implements MicroFragment<String>
{
    public final static Creator<EventLoaderMicroFragment> CREATOR = new Creator<EventLoaderMicroFragment>()
    {
        @Override
        public EventLoaderMicroFragment createFromParcel(Parcel source)
        {
            return new EventLoaderMicroFragment(source.readString());
        }


        @Override
        public EventLoaderMicroFragment[] newArray(int size)
        {
            return new EventLoaderMicroFragment[size];
        }
    };
    private final String mEventUid;


    public EventLoaderMicroFragment(String eventUid)
    {
        mEventUid = eventUid;
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
        Fragment result = new LoaderFragment();
        Bundle args = new Bundle(2);
        args.putParcelable(MicroFragment.ARG_ENVIRONMENT, new BasicMicroFragmentEnvironment<>(this, host));
        result.setArguments(args);
        return result;
    }


    @NonNull
    @Override
    public String parameters()
    {
        return mEventUid;
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
        dest.writeString(mEventUid);
    }


    public final static class LoaderFragment extends Fragment
    {
        private final Timestamp mTimestamp = new UiTimestamp();
        private ServiceJobQueue<ActionService> mActionServiceJobQueue;
        private ServiceJobQueue<ApiService> mApiServiceJobQueue;
        private String mEventUid;
        private Event mEvent;
        private List<Link> mActions;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mActionServiceJobQueue = new SimpleServiceJobQueue<>(new ActionService.FutureConnection(getActivity()));
            mApiServiceJobQueue = new SimpleServiceJobQueue<>(new ApiService.FutureConnection(getActivity()));
            mEventUid = new FragmentEnvironment<String>(this).microFragment().parameters();
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.schedjoules_fragment_event_details_loading, container, false);
            view.findViewById(android.R.id.message).animate().setStartDelay(1500).alpha(1).start();
            ((CollapsingToolbarLayout) view.findViewById(R.id.schedjoules_event_detail_toolbar_layout)).setTitle("Loading event …");

            if (Build.VERSION.SDK_INT < 21)
            {
                // on older Version the progressbar won't pick up the accent color, so we have to set it manually
                ((ProgressBar) view.findViewById(R.id.schedjoules_event_details_horizontal_actions_progressbar)).getIndeterminateDrawable()
                        .setColorFilter(getResources().getColor(R.color.schedjoules_colorAccent),
                                PorterDuff.Mode.MULTIPLY);
            }

            return view;
        }


        @Override
        public void onResume()
        {
            super.onResume();
            mApiServiceJobQueue.post(
                    new ServiceJob<ApiService>()
                    {
                        @Override
                        public void execute(ApiService service)
                        {
                            try
                            {
                                mEvent = service.apiResponse(new EventByUid(new StringToken(mEventUid))).payload();
                                loaderReady();
                            }
                            catch (URISyntaxException | ProtocolError | ProtocolException | IOException | RuntimeException e)
                            {
                                startTransition(new Faded(new ForwardTransition(new ErrorMicroFragment(), mTimestamp)));
                            }
                        }


                        @Override
                        public void onTimeOut()
                        {
                            startTransition(new Faded(new ForwardTransition(new ErrorMicroFragment(), mTimestamp)));
                        }
                    }, 5000);
            mActionServiceJobQueue.post(
                    new ServiceJob<ActionService>()
                    {
                        @Override
                        public void execute(ActionService service)
                        {
                            try
                            {
                                mActions = service.actions(mEventUid);
                            }
                            catch (TimeoutException | InterruptedException | ProtocolError | IOException | ProtocolException | URISyntaxException | RuntimeException e)
                            {
                                // TODO: remove RuntimeException when https://github.com/dmfs/http-client-essentials-suite/issues/36 is fixed
                                // actions could not be loaded - fall back to an empty list
                                mActions = Collections.emptyList();
                            }
                            loaderReady();
                        }


                        @Override
                        public void onTimeOut()
                        {
                            startTransition(new Faded(new ForwardTransition(new ErrorMicroFragment(), mTimestamp)));
                        }
                    }, 5000
            );
        }


        @Override
        public void onDestroy()
        {
            mApiServiceJobQueue.disconnect();
            mActionServiceJobQueue.disconnect();
            super.onDestroy();
        }


        private void loaderReady()
        {
            if (mEvent != null && mActions != null)
            {
                startTransition(new Faded(new ForwardTransition(new ShowEventMicroFragment(mEvent, mActions), mTimestamp)));
            }
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
