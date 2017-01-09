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
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.BaseFragment;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListBinding;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsImpl;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsProvider;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsProviderImpl;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.FlexibleAdapterNotifier;
import com.schedjoules.eventdiscovery.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.eventlist.view.EventListMenu;
import com.schedjoules.eventdiscovery.eventlist.view.EventListToolbar;
import com.schedjoules.eventdiscovery.eventlist.view.EventListView;
import com.schedjoules.eventdiscovery.location.ActivityForResultPlaceSelection;
import com.schedjoules.eventdiscovery.location.LastSelectedPlace;
import com.schedjoules.eventdiscovery.location.PlaceSelection;
import com.schedjoules.eventdiscovery.location.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.service.ApiService;
import com.schedjoules.eventdiscovery.utils.FutureLocalServiceConnection;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.utils.InsightsTask;

import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.rfc5545.DateTime;

import static com.schedjoules.eventdiscovery.EventIntents.EXTRA_GEOLOCATION;
import static com.schedjoules.eventdiscovery.EventIntents.EXTRA_START_AFTER_TIMESTAMP;


/**
 * Fragment for showing the event discovery list.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListFragment extends BaseFragment implements PlaceSelection.Listener, EventListMenu.Listener, EventListToolbar.Listener
{
    private FutureServiceConnection<ApiService> mApiService;
    private EventListItemsProvider mListItemsProvider;
    private ActivityForResultPlaceSelection mLocationSelection;
    private LastSelectedPlace mLastSelectedPlace;

    private EventListToolbar mToolbar;
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

        // Application context used now, if changed to Activity, this and disconnect() has to move to other callbacks
        mApiService = new FutureLocalServiceConnection<>(getContext().getApplicationContext(),
                new Intent("com.schedjoules.API").setPackage(getContext().getPackageName()));

        mListItemsProvider = new EventListItemsProviderImpl(mApiService, new EventListItemsImpl());

        mLocationSelection = new ActivityForResultPlaceSelection(this);
        mLocationSelection.registerListener(this);

        mLastSelectedPlace = new SharedPrefLastSelectedPlace(getContext());

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

        mToolbar = new EventListToolbar(views.schedjoulesEventListToolbar, this);
        mToolbar.initToolbar(getActivity());
        mToolbar.setToolbarTitle(mLastSelectedPlace.get().namedPlace().name());

        mListItemsProvider.setBackgroundMessageUI(
                new EventListBackgroundMessage(views.schedjoulesEventListBackgroundMessage));
        mListItemsProvider.setLoadingIndicatorUI(
                new EventListLoadingIndicatorOverlay(views.schedjoulesEventListProgressBar));

        EventListView eventListView = new EventListView(views.schedjoulesEventListInclude.schedjoulesEventList);
        eventListView.setEdgeScrollListener(mListItemsProvider);

        mListItemsProvider.setAdapterNotifier(new FlexibleAdapterNotifier(eventListView.getAdapter()));

        return views.getRoot();
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
        return mMenu.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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


    @Override
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mLocationSelection.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPlaceSelected(GeoPlace result)
    {
        mLastSelectedPlace.update(result);
        mToolbar.setToolbarTitle(result.namedPlace().name());
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
