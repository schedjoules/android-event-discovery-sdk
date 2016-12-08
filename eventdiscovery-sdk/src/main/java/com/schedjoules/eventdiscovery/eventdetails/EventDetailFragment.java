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

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.actions.Action;
import com.schedjoules.eventdiscovery.actions.ActionLoaderTask;
import com.schedjoules.eventdiscovery.actions.ActionViewIterable;
import com.schedjoules.eventdiscovery.actions.BaseActionFactory;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesEventDetailContentBinding;
import com.schedjoules.eventdiscovery.eventlist.EventListActivity;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskResult;
import com.schedjoules.eventdiscovery.model.ParcelableEvent;
import com.schedjoules.eventdiscovery.service.ApiService;
import com.schedjoules.eventdiscovery.utils.FutureLocalServiceConnection;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.utils.InsightsTask;
import com.schedjoules.eventdiscovery.utils.Limiting;
import com.schedjoules.eventdiscovery.utils.Skipping;

import org.dmfs.httpessentials.types.StringToken;

import java.util.Iterator;

import static com.schedjoules.eventdiscovery.utils.DateTimeFormatter.longDateFormat;
import static com.schedjoules.eventdiscovery.utils.DateTimeFormatter.longEventTimeFormat;
import static com.schedjoules.eventdiscovery.utils.LocationFormatter.longLocationFormat;


/**
 * A fragment representing a single Event detail screen. This fragment is either contained in a {@link EventListActivity} in two-pane mode (on tablets) or a
 * {@link EventDetailActivity} on handsets.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailFragment extends Fragment implements SafeAsyncTaskCallback<Void, Iterator<Iterable<Action>>>
{
    private static final String ARG_EVENT = "event";

    private FutureServiceConnection<ApiService> mApiService;
    private Event mEvent;

    private SchedjoulesEventDetailContentBinding mViews;
    private LinearLayout mVerticalItems;
    private HorizontalActionsView mHorizontalActions;


    public static Fragment newInstance(Event event)
    {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_EVENT, new ParcelableEvent(event));
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mEvent = getArguments().getParcelable(ARG_EVENT);

        mApiService = new FutureLocalServiceConnection<>(getContext(),
                new Intent("com.schedjoules.API").setPackage(getContext().getPackageName()));

        if (savedInstanceState == null)
        {
            new InsightsTask(getActivity()).execute(new Screen(new StringToken("details"), mEvent));
        }

        new ActionLoaderTask(mApiService, new BaseActionFactory(), this).execute(mEvent);

        mViews = DataBindingUtil.inflate(inflater, R.layout.schedjoules_event_detail_content, container, false);
        mVerticalItems = mViews.schedjoulesEventDetailVerticalItems;
        mHorizontalActions = mViews.schedjoulesEventHorizontalActions;

        addFixVerticalItems();
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


    private void removeHorizontalActionsView()
    {
        ((ViewGroup) mViews.getRoot()).removeView(mHorizontalActions);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mApiService.disconnect();
    }


    @Override
    public void onTaskFinish(SafeAsyncTaskResult<Iterator<Iterable<Action>>> result, Void o)
    {
        try
        {
            // there must be a next value
            Iterable<Action> actions = result.value().next();

            if (actions.iterator().hasNext())
            {
                int maxNumberOfItemsInTopBar = getResources().getInteger(
                        R.integer.schedjoules_maxNumberOfHorizontalActions);

                mHorizontalActions.showActionViews(
                        new ActionViewIterable(new Limiting<>(actions, maxNumberOfItemsInTopBar),
                                new SmallEventActionView.Factory(mHorizontalActions)));

                mViews.schedjoulesEventDetailsDivider.setVisibility(View.VISIBLE);
                mHorizontalActions.setVisibility(View.VISIBLE);

                for (View view : new ActionViewIterable(new Skipping<>(actions, maxNumberOfItemsInTopBar),
                        new EventDetailsItemView.Factory(mVerticalItems)))
                {
                    mVerticalItems.addView(view);
                    view.setAlpha(0);
                    view.animate().alpha(1).setDuration(500);
                }
            }
            else
            {
                // TODO: can we synthesize a few common actions that don't depend on the server response? I.e. share, add to calendar, directions
                removeHorizontalActionsView();
            }

        }
        catch (Exception e)
        {
            // TODO: can we synthesize a few common actions that don't depend on the server response?
            removeHorizontalActionsView();
        }

        mViews.schedjoulesEventDetailSchedjoulesFooter.animate().alpha(1).setStartDelay(500).setDuration(500);
    }
}
