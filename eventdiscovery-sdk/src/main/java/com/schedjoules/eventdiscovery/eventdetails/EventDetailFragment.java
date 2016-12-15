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

package com.schedjoules.eventdiscovery.eventdetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.actions.ActionViewIterable;
import com.schedjoules.eventdiscovery.actions.Actions;
import com.schedjoules.eventdiscovery.actions.BaseActionFactory;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesEventDetailContentBinding;
import com.schedjoules.eventdiscovery.eventlist.EventListActivity;
import com.schedjoules.eventdiscovery.model.ParcelableEvent;
import com.schedjoules.eventdiscovery.model.ParcelableLink;
import com.schedjoules.eventdiscovery.model.SchedJoulesLinks;
import com.schedjoules.eventdiscovery.utils.InsightsTask;
import com.schedjoules.eventdiscovery.utils.Limiting;
import com.schedjoules.eventdiscovery.utils.Skipping;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.StringToken;

import java.util.ArrayList;
import java.util.List;

import static com.schedjoules.eventdiscovery.utils.DateTimeFormatter.longDateFormat;
import static com.schedjoules.eventdiscovery.utils.DateTimeFormatter.longEventTimeFormat;
import static com.schedjoules.eventdiscovery.utils.LocationFormatter.longLocationFormat;


/**
 * A fragment representing a single Event detail screen. This fragment is either contained in a {@link EventListActivity} in two-pane mode (on tablets) or a
 * {@link EventDetailActivity} on handsets.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailFragment extends Fragment
{
    private static final String ARG_EVENT = "event";
    private static final String ARG_ACTIONS = "actions";

    private Event mEvent;
    private List<ParcelableLink> mActions;

    private SchedjoulesEventDetailContentBinding mViews;
    private LinearLayout mVerticalItems;
    private HorizontalActionsView mHorizontalActions;


    public static Fragment newInstance(Event event, List<Link> actionLinks)
    {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_EVENT, new ParcelableEvent(event));

        ArrayList<Parcelable> links = new ArrayList<>(actionLinks.size());
        for (Link actionLink : actionLinks)
        {
            links.add(actionLink instanceof Parcelable ? (Parcelable) actionLink : new ParcelableLink(actionLink));
        }
        arguments.putParcelableArrayList(ARG_ACTIONS, links);
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mEvent = getArguments().getParcelable(ARG_EVENT);
        mActions = getArguments().getParcelableArrayList(ARG_ACTIONS);

        if (savedInstanceState == null)
        {
            new InsightsTask(getActivity()).execute(new Screen(new StringToken("details"), mEvent));
        }

        mViews = DataBindingUtil.inflate(inflater, R.layout.schedjoules_event_detail_content, container, false);
        mVerticalItems = mViews.schedjoulesEventDetailVerticalItems;
        mHorizontalActions = mViews.schedjoulesEventHorizontalActions;
        mViews.schedjoulesDetailsHeader.schedjoulesEventDetailToolbarLayout.setTitle(mEvent.title());
        addFixVerticalItems();
        showActions();
        Glide.with(getActivity())
                .load(new SchedJoulesLinks(mEvent.links()).bannerUri())
                .into(mViews.schedjoulesDetailsHeader.schedjoulesEventDetailBanner);
        return mViews.getRoot();
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


    private void showActions()
    {
        if (mActions.size() > 0)
        {
            int maxNumberOfItemsInTopBar = getResources().getInteger(R.integer.schedjoules_maxNumberOfHorizontalActions);

            mHorizontalActions.showActionViews(
                    new ActionViewIterable(new Limiting<>(new Actions(mActions, mEvent, new BaseActionFactory()), maxNumberOfItemsInTopBar),
                            new SmallEventActionView.Factory(mHorizontalActions)));

            mViews.schedjoulesEventDetailsDivider.setVisibility(View.VISIBLE);
            mHorizontalActions.setVisibility(View.VISIBLE);

            for (View view : new ActionViewIterable(new Skipping<>(new Actions(mActions, mEvent, new BaseActionFactory()), maxNumberOfItemsInTopBar),
                    new EventDetailsItemView.Factory(mVerticalItems)))
            {
                mVerticalItems.addView(view);
            }
        }
        else
        {
            // hide actions bar
            mViews.schedjoulesEventHorizontalActions.setVisibility(View.GONE);
        }
    }
}
