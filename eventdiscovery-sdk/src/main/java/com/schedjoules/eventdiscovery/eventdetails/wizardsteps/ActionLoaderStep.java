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
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesEventDetailContentLoadingActionsBinding;
import com.schedjoules.eventdiscovery.eventdetails.EventDetailsItemView;
import com.schedjoules.eventdiscovery.eventdetails.EventDetailsTwoLineItemView;
import com.schedjoules.eventdiscovery.eventdetails.transitions.AutomaticWizardTransition;
import com.schedjoules.eventdiscovery.model.ParcelableEvent;
import com.schedjoules.eventdiscovery.model.SchedJoulesLinks;
import com.schedjoules.eventdiscovery.service.ActionService;
import com.schedjoules.eventdiscovery.service.ServiceJob;
import com.schedjoules.eventdiscovery.service.ServiceJobQueue;
import com.schedjoules.eventdiscovery.service.SimpleServiceJobQueue;

import org.dmfs.android.dumbledore.WizardStep;
import org.dmfs.android.dumbledore.transitions.WizardTransition;
import org.dmfs.httpessentials.types.StringToken;

import static com.schedjoules.eventdiscovery.R.layout.schedjoules_event_detail_content_loading_actions;
import static com.schedjoules.eventdiscovery.utils.DateTimeFormatter.longDateFormat;
import static com.schedjoules.eventdiscovery.utils.DateTimeFormatter.longEventTimeFormat;
import static com.schedjoules.eventdiscovery.utils.LocationFormatter.longLocationFormat;


/**
 * A {@link WizardStep} that loads the actions of a given event.
 */
public final class ActionLoaderStep implements WizardStep
{
    private final Event mEvent;


    public ActionLoaderStep(Event event)
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
    public Fragment fragment(@NonNull Context context)
    {
        Fragment result = new LoaderFragment();
        Bundle args = new Bundle(2);
        args.putParcelable(WizardStep.ARG_WIZARD_STEP, this);
        args.putParcelable("event", mEvent instanceof Parcelable ? (Parcelable) mEvent : new ParcelableEvent(mEvent));
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
        dest.writeParcelable(mEvent instanceof Parcelable ? (Parcelable) mEvent : new ParcelableEvent(mEvent), flags);
    }


    public final static Creator<ActionLoaderStep> CREATOR = new Creator<ActionLoaderStep>()
    {
        @Override
        public ActionLoaderStep createFromParcel(Parcel source)
        {
            return new ActionLoaderStep((Event) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public ActionLoaderStep[] newArray(int size)
        {
            return new ActionLoaderStep[size];
        }
    };


    public final static class LoaderFragment extends Fragment
    {
        private ServiceJobQueue<ActionService> mServiceJobQueue;
        private Event mEvent;
        private LinearLayout mVerticalItems;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mServiceJobQueue = new SimpleServiceJobQueue<>(new ActionService.FutureConnection(getActivity()));
            mEvent = getArguments().getParcelable("event");
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            SchedjoulesEventDetailContentLoadingActionsBinding mViews = DataBindingUtil.inflate(inflater, schedjoules_event_detail_content_loading_actions,
                    container, false);

            mViews.header.schedjoulesEventDetailToolbarLayout.setTitle(mEvent.title());
            mVerticalItems = mViews.schedjoulesEventDetailVerticalItems;

            addFixVerticalItems();
            // we already have the event, so load and show the image right away
            Glide.with(getActivity())
                    .load(new SchedJoulesLinks(mEvent.links()).bannerUri())
                    .into(mViews.header.schedjoulesEventDetailBanner);
            return mViews.getRoot();
        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            mServiceJobQueue.post(new ServiceJob<ActionService>()
            {
                @Override
                public void execute(ActionService service)
                {
                    advanceWizard(new AutomaticWizardTransition(new ShowEventStep(mEvent, service.actions(new StringToken(mEvent.uid())))));
                }


                @Override
                public void onTimeOut()
                {
                    advanceWizard(new AutomaticWizardTransition(new ErrorStep()));
                }
            }, 5000);
        }


        @Override
        public void onDestroy()
        {
            mServiceJobQueue.disconnect();
            super.onDestroy();
        }


        private void advanceWizard(WizardTransition wizardTransition)
        {
            Activity activity = getActivity();
            if (activity != null)
            {
                wizardTransition.execute(activity);
            }
        }


        // TODO Could be factored out declaratively to sg like EventDetailItems implements Iterator<View>.
        // (And create LinearLayout with addViews(Iterator<View> views) for mVerticalItems (would be useful in onResult, too))
        private void addFixVerticalItems()
        {
            EventDetailsTwoLineItemView dateTimeItem = EventDetailsTwoLineItemView.inflate(mVerticalItems);
            dateTimeItem.setIcon(R.drawable.schedjoules_ic_time);
            dateTimeItem.setTitle(longDateFormat(mEvent.start()));
            dateTimeItem.setSubTitle(longEventTimeFormat(getContext(), mEvent));
            mVerticalItems.addView(dateTimeItem);

            if (mEvent.locations().iterator().hasNext())
            {
                EventDetailsItemView locationItem = EventDetailsItemView.inflate(mVerticalItems);
                locationItem.setIcon(R.drawable.schedjoules_ic_location);
                locationItem.setTextAsTitle(longLocationFormat(mEvent.locations()));
                mVerticalItems.addView(locationItem);
            }

            if (!TextUtils.isEmpty(mEvent.description()))
            {
                EventDetailsItemView descriptionItem = EventDetailsItemView.inflate(mVerticalItems);
                descriptionItem.setIcon(R.drawable.schedjoules_ic_description);
                descriptionItem.setTextAsTitle(mEvent.description());
                mVerticalItems.addView(descriptionItem);
            }

        }
    }
}
