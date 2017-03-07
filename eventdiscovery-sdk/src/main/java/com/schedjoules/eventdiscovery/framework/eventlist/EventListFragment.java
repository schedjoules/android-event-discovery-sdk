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

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListBinding;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.common.ExternalUrlFeedbackForm;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListController;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListControllerImpl;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.FlexibleAdapterEventListItems;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EdgeReachScrollListener;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListMenu;
import com.schedjoules.eventdiscovery.framework.location.ActivityForResultPlaceSelection;
import com.schedjoules.eventdiscovery.framework.location.LastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.location.PlaceSelection;
import com.schedjoules.eventdiscovery.framework.location.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.framework.utils.InsightsTask;
import com.schedjoules.eventdiscovery.framework.widgets.TextWithIcon;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.rfc5545.DateTime;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

import static com.schedjoules.eventdiscovery.framework.EventIntents.EXTRA_GEOLOCATION;
import static com.schedjoules.eventdiscovery.framework.EventIntents.EXTRA_START_AFTER_TIMESTAMP;


/**
 * Fragment for showing the event discovery list.
 *
 * @author Gabor Keszthelyi
 */

public final class EventListFragment extends BaseFragment implements PlaceSelection.Listener, EventListMenu.Listener
{
    private FutureServiceConnection<ApiService> mApiService;
    private EventListController mListItemsController;
    private ActivityForResultPlaceSelection mLocationSelection;
    private LastSelectedPlace mLastSelectedPlace;

    private TextView mToolbarTitle;
    private EventListMenu mMenu;

    private boolean mIsInitializing;
    private FlexibleAdapter mAdapter;
    private SchedjoulesFragmentEventListBinding mViews;


    public static Fragment newInstance(Bundle args)
    {
        EventListFragment fragment = new EventListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mIsInitializing = true;

        mApiService = new ApiService.FutureConnection(getActivity());

        mListItemsController = new EventListControllerImpl(mApiService, new FlexibleAdapterEventListItems());

        mLocationSelection = new ActivityForResultPlaceSelection(this);
        mLocationSelection.registerListener(this);

        mLastSelectedPlace = new SharedPrefLastSelectedPlace(getContext());

        new InsightsTask(getActivity()).execute(new Screen(new StringToken("list")));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mViews = DataBindingUtil.inflate(inflater,
                R.layout.schedjoules_fragment_event_list, container, false);

        mMenu = new EventListMenu(this);
        setHasOptionsMenu(true);

        setupToolbar(mViews);

        mListItemsController.setBackgroundMessageUI(
                new EventListBackgroundMessage(mViews.schedjoulesEventListBackgroundMessage));
        mListItemsController.setLoadingIndicatorUI(
                new EventListLoadingIndicatorOverlay(mViews.schedjoulesEventListProgressBar));

        initAdapterAndRecyclerView(true);

        return mViews.getRoot();
    }


    private void setupToolbar(SchedjoulesFragmentEventListBinding views)
    {
        Toolbar toolbar = views.schedjoulesEventListToolbar;
        CharSequence lastSelectedPlace = mLastSelectedPlace.get().namedPlace().name();
        toolbar.setTitle(""); // Need to set it to empty, otherwise the activity label is set automatically

        mToolbarTitle = views.schedjoulesEventListToolbarTitle;
        mToolbarTitle.setText(
                new TextWithIcon(getContext(), lastSelectedPlace, R.drawable.schedjoules_ic_arrow_drop_down_white));
        mToolbarTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onToolbarTitleClick();
            }
        });

        BaseActivity activity = (BaseActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        Resources res = activity.getResources();
        toolbar.setContentInsetsAbsolute(res.getDimensionPixelSize(R.dimen.schedjoules_list_item_padding_horizontal),
                toolbar.getContentInsetRight());

        if (res.getBoolean(R.bool.schedjoules_enableBackArrowOnEventListScreen))
        {
            //noinspection ConstantConditions
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void initAdapterAndRecyclerView(boolean restoring)
    {
        createAdapter(restoring);

        RecyclerView recyclerView = mViews.schedjoulesEventListInclude.schedjoulesEventList;
        recyclerView.setAdapter(mAdapter);

        EdgeReachScrollListener scrollListener = new EdgeReachScrollListener(recyclerView, mListItemsController,
                EventListControllerImpl.CLOSE_TO_TOP_OR_BOTTOM_THRESHOLD);
        recyclerView.addOnScrollListener(scrollListener);

        mListItemsController.setAdapter(mAdapter);
    }


    private void createAdapter(boolean restoring)
    {
        if (mAdapter == null || !restoring)
        {
            mAdapter = createNewFlexibleAdapter();
        }
        else
        {
            List<IFlexible> currentItems = new ArrayList<>(mAdapter.getItemCount());
            for (int i = 0; i < mAdapter.getItemCount(); i++)
            {
                currentItems.add(mAdapter.getItem(i));
            }

            FlexibleAdapter<IFlexible> newAdapter = createNewFlexibleAdapter();
            newAdapter.addItems(0, currentItems);

            mAdapter = newAdapter;
        }
    }


    private FlexibleAdapter<IFlexible> createNewFlexibleAdapter()
    {
        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(new ArrayList<IFlexible>());
        adapter.setDisplayHeadersAtStartUp(true);
        adapter.setStickyHeaders(true);
        return adapter;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (mIsInitializing)
        {
            mIsInitializing = false;
            // Can only be started currently after UI is initialized (so not in onCreate()) because it updates it
            mListItemsController.loadEvents(location(), startAfter());
        }
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


    @Override
    public void onUpButtonClick()
    {
        // TODO revisit this if navigation/screen layouts change (tablet)
        if (getActivity() != null)
        {
            getActivity().finish();
        }
    }


    public void onToolbarTitleClick()
    {
        mLocationSelection.initiateSelection();
    }


    @Override
    public void onFeedbackMenuClick()
    {
        new ExternalUrlFeedbackForm().show(getActivity());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mLocationSelection.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPlaceSelected(GeoPlace result)
    {
        initAdapterAndRecyclerView(false);
        mLastSelectedPlace.update(result);
        mToolbarTitle.setText(new TextWithIcon(getContext(), result.namedPlace().name(), R.drawable.schedjoules_ic_arrow_drop_down_white));
        mListItemsController.loadEvents(result.geoLocation(), startAfter());
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mApiService.disconnect();
        mLocationSelection.unregisterListener();
    }


    private GeoLocation location()
    {
        if (getArguments() != null && getArguments().containsKey(EXTRA_GEOLOCATION))
        {
            // TODO Save location and update Toolbar title with name when Event Discovery for input geo-location is actually supported
            return getArguments().getParcelable(EXTRA_GEOLOCATION);
        }
        else
        {
            return mLastSelectedPlace.get().geoLocation();
        }
    }


    private DateTime startAfter()
    {
        if (getArguments() != null && getArguments().containsKey(EXTRA_START_AFTER_TIMESTAMP))
        {
            long startAfterTimeStamp = getArguments().getLong(EXTRA_START_AFTER_TIMESTAMP, 0);
            return new DateTime(startAfterTimeStamp);
        }
        else
        {
            return DateTime.nowAndHere();
        }
    }
}
