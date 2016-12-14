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

package com.schedjoules.eventdiscovery.eventdetails.wizardsteps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.queries.EventByUid;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.eventdetails.transitions.AutomaticWizardTransition;
import com.schedjoules.eventdiscovery.service.ActionService;
import com.schedjoules.eventdiscovery.service.ApiService;
import com.schedjoules.eventdiscovery.service.ServiceJob;
import com.schedjoules.eventdiscovery.service.ServiceJobQueue;
import com.schedjoules.eventdiscovery.service.SimpleServiceJobQueue;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.dmfs.android.dumbledore.WizardStep;
import org.dmfs.android.dumbledore.transitions.WizardTransition;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.StringToken;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;


/**
 * A {@link WizardStep} that loads an event given by UID.
 */
public final class EventLoaderStep implements WizardStep
{
    private final String mEventUid;


    public EventLoaderStep(String eventUid)
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
    public Fragment fragment(@NonNull Context context)
    {
        Fragment result = new LoaderFragment();
        Bundle args = new Bundle(2);
        args.putParcelable(WizardStep.ARG_WIZARD_STEP, this);
        args.putString("eventUid", mEventUid);
        result.setArguments(args);
        return result;
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


    public final static Creator<EventLoaderStep> CREATOR = new Creator<EventLoaderStep>()
    {
        @Override
        public EventLoaderStep createFromParcel(Parcel source)
        {
            return new EventLoaderStep(source.readString());
        }


        @Override
        public EventLoaderStep[] newArray(int size)
        {
            return new EventLoaderStep[size];
        }
    };


    public final static class LoaderFragment extends Fragment
    {
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
            mApiServiceJobQueue = new SimpleServiceJobQueue<ApiService>(new ApiService.FutureConnection(getActivity()));
            mEventUid = getArguments().getString("eventUid");
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.schedjoules_fragment_loader, container, false);
            view.findViewById(android.R.id.message).animate().setStartDelay(1500).alpha(1).start();
            ((CollapsingToolbarLayout) view.findViewById(R.id.schedjoules_event_detail_toolbar_layout)).setTitle("Loading event …");
            return view;
        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            // TODO: load actions in parallel
            mApiServiceJobQueue.post(new ServiceJob<ApiService>()
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
                        advanceWizard(new AutomaticWizardTransition(new ErrorStep()));
                    }
                }


                @Override
                public void onTimeOut()
                {
                    advanceWizard(new AutomaticWizardTransition(new ErrorStep()));
                }
            }, 5000);
            mActionServiceJobQueue.post(
                    new ServiceJob<ActionService>()
                    {
                        @Override
                        public void execute(ActionService service)
                        {
                            mActions = service.actions(new StringToken(mEventUid));
                            if (mActions == null)
                            {
                                mActions = Collections.emptyList();
                            }
                            loaderReady();
                        }


                        @Override
                        public void onTimeOut()
                        {
                            advanceWizard(new AutomaticWizardTransition(new ErrorStep()));
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
                advanceWizard(new AutomaticWizardTransition(new ShowEventStep(mEvent, mActions)));
            }
        }


        private void advanceWizard(WizardTransition wizardTransition)
        {
            Activity activity = getActivity();
            if (activity != null)
            {
                wizardTransition.execute(activity);
            }
        }
    }
}
