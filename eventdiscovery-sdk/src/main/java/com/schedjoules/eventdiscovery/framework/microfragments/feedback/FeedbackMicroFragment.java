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

package com.schedjoules.eventdiscovery.framework.microfragments.feedback;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.queries.FeedbackUrl;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ErrorMicroFragment;
import com.schedjoules.eventdiscovery.framework.microfragments.webview.WebviewMicroFragment;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJob;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJobQueue;
import com.schedjoules.eventdiscovery.framework.utils.SimpleServiceJobQueue;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.android.microfragments.BasicMicroFragmentEnvironment;
import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.UiTimestamp;
import org.dmfs.android.microfragments.transitions.BackTransition;
import org.dmfs.android.microfragments.transitions.Faded;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * A {@link MicroFragment} that loads Feedback Form
 */
public final class FeedbackMicroFragment implements MicroFragment<Void>
{
    public final static Creator<FeedbackMicroFragment> CREATOR = new Creator<FeedbackMicroFragment>()
    {
        @Override
        public FeedbackMicroFragment createFromParcel(Parcel source)
        {
            return new FeedbackMicroFragment();
        }


        @Override
        public FeedbackMicroFragment[] newArray(int size)
        {
            return new FeedbackMicroFragment[size];
        }
    };


    public FeedbackMicroFragment()
    {

    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        return context.getString(R.string.schedjoules_menu_feedback);
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, MicroFragmentHost host)
    {
        Fragment result = new LoaderFragment();
        Bundle args = new Bundle(1);
        args.putParcelable(MicroFragment.ARG_ENVIRONMENT, new BasicMicroFragmentEnvironment<>(this, host));
        result.setArguments(args);
        return result;
    }


    @NonNull
    @Override
    public Void parameters()
    {
        return null;
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
    }


    public final static class LoaderFragment extends Fragment
    {
        private final Timestamp mTimestamp = new UiTimestamp();
        private ServiceJobQueue<ApiService> mApiServiceJobQueue;
        private MicroFragmentEnvironment<Void> mEnvironment;
        private long mFragmentStartTime;


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
            mEnvironment = new FragmentEnvironment<>(this);

            View root = inflater.inflate(R.layout.schedjoules_fragment_loading_feedback, container, false);

            Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
            toolbar.setTitle(mEnvironment.microFragment().title(getActivity()));

            toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mEnvironment.host().execute(getActivity(), new BackTransition());
                }
            });
            mFragmentStartTime = System.currentTimeMillis();
            return root;
        }


        @Override
        public void onResume()
        {
            super.onResume();
            mApiServiceJobQueue.post(new ServiceJob<ApiService>()
            {
                @Override
                public void execute(ApiService service)
                {
                    try
                    {
                        URI url = service.apiResponse(new FeedbackUrl());
                        long duration = System.currentTimeMillis() - mFragmentStartTime;
                        int animationTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
                        if (duration < animationTime)
                        {
                            // wait until the transition animation finishes before staring the new one
                            Thread.sleep(animationTime - duration);
                        }

                        startTransition(
                                new Faded(
                                        new ForwardTransition(
                                                new WebviewMicroFragment(
                                                        mEnvironment.microFragment().title(getActivity()), url),
                                                mTimestamp)));
                    }
                    catch (ProtocolError | IOException | ProtocolException | URISyntaxException | RuntimeException e)
                    {
                        startTransition(new Faded(new ForwardTransition(new ErrorMicroFragment(), mTimestamp)));
                    }
                    catch (InterruptedException e)
                    {
                        // this thread has been interrupted, so ignore any result and don't do anything
                        Thread.interrupted();
                    }
                }


                @Override
                public void onTimeOut()
                {
                    startTransition(new Faded(new ForwardTransition(new ErrorMicroFragment(), mTimestamp)));
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
