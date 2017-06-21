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

package com.schedjoules.eventdiscovery.framework.eventlist;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.InitialEventsDiscovery;
import com.schedjoules.eventdiscovery.framework.locationpicker.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.EventResultPageBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BundleBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.splash.SplashErrorMicroFragment;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJob;
import com.schedjoules.eventdiscovery.framework.utils.SimpleServiceJobQueue;
import com.schedjoules.eventdiscovery.service.ApiService;

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
import org.dmfs.rfc5545.DateTime;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * {@link MicroFragment} for the loading screen before showing the event list.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListLoaderMicroFragment implements MicroFragment<Bundle>
{
    private final Bundle mArgs;


    public EventListLoaderMicroFragment(Bundle args)
    {
        mArgs = args;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        throw new UnsupportedOperationException("Title is not used for this fragment");
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host)
    {
        return new LoaderFragment();
    }


    @NonNull
    @Override
    public Bundle parameter()
    {
        return mArgs;
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
        dest.writeBundle(mArgs);
    }


    public static final Creator<EventListLoaderMicroFragment> CREATOR = new Parcelable.Creator<EventListLoaderMicroFragment>()
    {
        @Override
        public EventListLoaderMicroFragment createFromParcel(Parcel in)
        {
            return new EventListLoaderMicroFragment(in.readBundle(getClass().getClassLoader()));
        }


        @Override
        public EventListLoaderMicroFragment[] newArray(int size)
        {
            return new EventListLoaderMicroFragment[size];
        }
    };


    public static final class LoaderFragment extends BaseFragment
    {
        private final Timestamp mTimestamp = new UiTimestamp();

        private SimpleServiceJobQueue<ApiService> mApiServiceJobQueue;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mApiServiceJobQueue = new SimpleServiceJobQueue<>(new ApiService.FutureConnection(getActivity()));
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            return inflater.inflate(R.layout.schedjoules_fragment_event_list_loader, container, false);
        }


        @Override
        public void onResume()
        {
            super.onResume();
            final Bundle incomingArgs = new FragmentEnvironment<Bundle>(this).microFragment().parameter();
            DateTime startAfter = new OptionalArgument<>(Keys.DATE_TIME_START_AFTER, incomingArgs).value(DateTime.nowAndHere());
            GeoLocation location = new SharedPrefLastSelectedPlace(getContext()).get().geoLocation();
            final InitialEventsDiscovery query = new InitialEventsDiscovery(startAfter, location);

            mApiServiceJobQueue.post(new ServiceJob<ApiService>()
            {
                @Override
                public void execute(ApiService service)
                {
                    try
                    {
                        ResultPage<Envelope<Event>> resultPage = service.apiResponse(query);
                        Bundle args = new BundleBuilder(incomingArgs).with(Keys.EVENTS_RESULT_PAGE, new EventResultPageBox(resultPage)).build();
                        startTransition(new Faded(new ForwardTransition(new EventListMicroFragment(args), mTimestamp)));
                    }
                    catch (ProtocolError | IOException | ProtocolException | URISyntaxException | RuntimeException e)
                    {
                        onError();
                    }
                }


                @Override
                public void onTimeOut()
                {
                    onError();
                }


                private void onError()
                {
                    startTransition(new Faded(new ForwardTransition<>(
                            new SplashErrorMicroFragment(new FragmentEnvironment<Bundle>(LoaderFragment.this).microFragment().parameter()),
                            mTimestamp)));
                }
            }, 5000);
        }


        @Override
        public void onDestroy()
        {
            mApiServiceJobQueue.disconnect();
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
