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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesEventListBinding;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListController;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListControllerImpl;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.FlexibleAdapterEventListItems;
import com.schedjoules.eventdiscovery.framework.eventlist.flexibleadapter.Copying;
import com.schedjoules.eventdiscovery.framework.eventlist.flexibleadapter.FlexibleAdapterFactory;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EdgeReachScrollListener;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.framework.locationpicker.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.rfc5545.DateTime;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * Fragment for the event item list on the list screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListListFragment extends BaseFragment
{
    private FutureServiceConnection<ApiService> mApiService;
    private EventListController mListItemsController;
    private FlexibleAdapter<IFlexible> mAdapter;
    private SchedjoulesEventListBinding mViews;


    public static Fragment newInstance(Bundle args)
    {
        EventListListFragment fragment = new EventListListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mApiService = new ApiService.FutureConnection(getActivity());

        mListItemsController = new EventListControllerImpl(mApiService, new FlexibleAdapterEventListItems());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mViews = DataBindingUtil.inflate(inflater, R.layout.schedjoules_event_list, container, false);

        mListItemsController.setBackgroundMessageUI(
                new EventListBackgroundMessage(mViews.schedjoulesEventListBackgroundMessage));
        mListItemsController.setLoadingIndicatorUI(
                new EventListLoadingIndicatorOverlay(mViews.schedjoulesEventListProgressBar));

        update(savedInstanceState == null);

        return mViews.getRoot();
    }


    private void update(boolean freshList)
    {
        initAdapterAndRecyclerView(freshList);
        if (freshList)
        {
            mListItemsController.loadEvents(location(), startAfter());
        }
    }


    private void initAdapterAndRecyclerView(boolean freshList)
    {
        Factory<FlexibleAdapter<IFlexible>> adapterFactory = new FlexibleAdapterFactory();
        if (!freshList && mAdapter != null)
        {
            adapterFactory = new Copying(adapterFactory, mAdapter);
        }
        FlexibleAdapter<IFlexible> adapter = adapterFactory.create();

        RecyclerView recyclerView = mViews.schedjoulesEventList;
        recyclerView.setAdapter(adapter);
        adapter.setStickyHeaders(true); // Better to set it after adapter has been set to RecyclerView
        recyclerView.addOnScrollListener(
                new EdgeReachScrollListener(recyclerView, mListItemsController,
                        EventListControllerImpl.CLOSE_TO_TOP_OR_BOTTOM_THRESHOLD));

        mListItemsController.setAdapter(adapter);
        mAdapter = adapter;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mApiService.disconnect();
    }


    private GeoLocation location()
    {
        if (new OptionalArgument<>(Keys.GEO_LOCATION, getArguments()).isPresent())
        {
            // TODO Save location and update Toolbar title with name when Event Discovery for input geo-location is actually supported
            throw new UnsupportedOperationException("Discovery for a geo location not supported yet.");
        }
        return new SharedPrefLastSelectedPlace(getContext()).get().geoLocation();
    }


    private DateTime startAfter()
    {
        return new OptionalArgument<>(Keys.DATE_TIME_START_AFTER, this).value(DateTime.nowAndHere());
    }

}
