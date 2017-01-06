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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.schedjoules.eventdiscovery.common.BaseActivity;
import com.schedjoules.eventdiscovery.common.BaseFragment;
import com.schedjoules.eventdiscovery.common.ExternalUrlFeedbackForm;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventDetailsBinding;
import com.schedjoules.eventdiscovery.datetime.LongDate;
import com.schedjoules.eventdiscovery.datetime.StartAndEndTime;
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

import static com.schedjoules.eventdiscovery.utils.LocationFormatter.longLocationFormat;


/**
 * A fragment representing a single Event detail screen. This fragment is either contained in a {@link
 * EventListActivity} in two-pane mode (on tablets) or a {@link EventDetailsActivity} on handsets.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsFragment extends BaseFragment implements EventDetailsMenu.Listener
{
    private static final String ARG_EVENT = "event";
    private static final String ARG_ACTIONS = "actions";

    private Event mEvent;
    private List<ParcelableLink> mActions;

    private SchedjoulesFragmentEventDetailsBinding mViews;
    private LinearLayout mVerticalItems;
    private HorizontalActionsView mHorizontalActions;
    private EventDetailsMenu mMenu;


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
        EventDetailsFragment fragment = new EventDetailsFragment();
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

        mViews = DataBindingUtil.inflate(inflater, R.layout.schedjoules_fragment_event_details, container, false);

        ((BaseActivity) getActivity()).setSupportActionBar(
                mViews.schedjoulesDetailsHeader.schedjoulesEventDetailToolbar);
        setHasOptionsMenu(true);
        mMenu = new EventDetailsMenu(this);

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        mMenu.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return mMenu.onOptionsItemSelected(item);
    }


    // TODO Could be factored out declaratively to sg like EventDetailItems implements Iterator<View>.
    // (And create LinearLayout with addViews(Iterator<View> views) for mVerticalItems (would be useful in onResult, too))
    private void addFixVerticalItems()
    {
        EventDetailsTwoLineItemView dateTimeItem = EventDetailsTwoLineItemView.inflate(mVerticalItems);
        dateTimeItem.setIcon(R.drawable.schedjoules_ic_time);
        dateTimeItem.setTitle(new LongDate(mEvent.start()).value(getContext()));
        dateTimeItem.setSubTitle(new StartAndEndTime(mEvent).value(getContext()));
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
            int maxNumberOfItemsInTopBar = getResources().getInteger(
                    R.integer.schedjoules_maxNumberOfHorizontalActions);

            mHorizontalActions.showActionViews(
                    new ActionViewIterable(new Limiting<>(new Actions(mActions, mEvent, new BaseActionFactory()),
                            maxNumberOfItemsInTopBar),
                            new SmallEventActionView.Factory(mHorizontalActions)));

            mViews.schedjoulesEventDetailsDivider.setVisibility(View.VISIBLE);
            mHorizontalActions.setVisibility(View.VISIBLE);

            for (View view : new ActionViewIterable(
                    new Skipping<>(new Actions(mActions, mEvent, new BaseActionFactory()), maxNumberOfItemsInTopBar),
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


    @Override
    public void onFeedbackMenuClick()
    {
        new ExternalUrlFeedbackForm().show(getActivity());
    }
}
