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

package com.schedjoules.eventdiscovery.eventlist;

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

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.BaseActivity;
import com.schedjoules.eventdiscovery.common.BaseFragment;
import com.schedjoules.eventdiscovery.common.ExternalUrlFeedbackForm;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListBinding;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsImpl;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsProvider;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsProviderImpl;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.FlexibleAdapterNotifier;
import com.schedjoules.eventdiscovery.eventlist.view.EdgeReachScrollListener;
import com.schedjoules.eventdiscovery.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.eventlist.view.EventListMenu;
import com.schedjoules.eventdiscovery.location.LastSelectedLocation;
import com.schedjoules.eventdiscovery.location.LocationSelection;
import com.schedjoules.eventdiscovery.location.LocationSelectionResult;
import com.schedjoules.eventdiscovery.location.PlacesApiLocationSelection;
import com.schedjoules.eventdiscovery.location.SharedPrefLastSelectedLocation;
import com.schedjoules.eventdiscovery.service.ApiService;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.utils.InsightsTask;

import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.rfc5545.DateTime;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

import static com.schedjoules.eventdiscovery.EventIntents.EXTRA_GEOLOCATION;
import static com.schedjoules.eventdiscovery.EventIntents.EXTRA_START_AFTER_TIMESTAMP;


/**
 * Fragment for showing the event discovery list.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListFragment extends BaseFragment implements LocationSelection.Listener, EventListMenu.Listener
{
    private FutureServiceConnection<ApiService> mApiService;
    private EventListItemsProvider mListItemsProvider;
    private PlacesApiLocationSelection mLocationSelection;
    private LastSelectedLocation mLastSelectedLocation;

    private Toolbar mToolbar;
    private EventListMenu mMenu;

    private boolean mIsInitializing;


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

        mListItemsProvider = new EventListItemsProviderImpl(mApiService, new EventListItemsImpl());

        mLocationSelection = new PlacesApiLocationSelection(this);
        mLocationSelection.registerListener(this);

        mLastSelectedLocation = new SharedPrefLastSelectedLocation(getContext());

        new InsightsTask(getActivity()).execute(new Screen(new StringToken("list")));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        SchedjoulesFragmentEventListBinding views = DataBindingUtil.inflate(inflater,
                R.layout.schedjoules_fragment_event_list, container, false);

        mMenu = new EventListMenu(this);
        setHasOptionsMenu(true);

        mToolbar = views.schedjoulesEventListToolbar;
        setupToolbar();

        mListItemsProvider.setBackgroundMessageUI(
                new EventListBackgroundMessage(views.schedjoulesEventListBackgroundMessage));
        mListItemsProvider.setLoadingIndicatorUI(
                new EventListLoadingIndicatorOverlay(views.schedjoulesEventListProgressBar));

        FlexibleAdapter adapter = createAdapter();

        RecyclerView recyclerView = views.schedjoulesEventListInclude.schedjoulesEventList;
        recyclerView.setAdapter(adapter);

        EdgeReachScrollListener scrollListener = new EdgeReachScrollListener(recyclerView, mListItemsProvider,
                EventListItemsProviderImpl.CLOSE_TO_TOP_OR_BOTTOM_THRESHOLD);
        recyclerView.addOnScrollListener(scrollListener);

        mListItemsProvider.setAdapterNotifier(new FlexibleAdapterNotifier(adapter));

        return views.getRoot();
    }


    private void setupToolbar()
    {
        mToolbar.setTitle(mLastSelectedLocation.get().name());
        mToolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onToolbarTitleClick();
            }
        });

        BaseActivity activity = (BaseActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        Resources res = activity.getResources();
        if (res.getBoolean(R.bool.schedjoules_enableBackArrowOnEventListScreen))
        {
            //noinspection ConstantConditions
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else
        {
            mToolbar.setTitleMarginStart(res.getDimensionPixelSize(R.dimen.schedjoules_list_item_horizontal_margin));
        }
    }


    private FlexibleAdapter createAdapter()
    {
        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(null);
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
            mListItemsProvider.loadEvents(location(), startAfter());
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
    public void onLocationMenuIconClick()
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
    public void onLocationSelected(LocationSelectionResult result)
    {
        mLastSelectedLocation.update(result);
        mToolbar.setTitle(result.name());
        mListItemsProvider.loadEvents(result.geoLocation(), startAfter());
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
            return mLastSelectedLocation.get().geoLocation();
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
