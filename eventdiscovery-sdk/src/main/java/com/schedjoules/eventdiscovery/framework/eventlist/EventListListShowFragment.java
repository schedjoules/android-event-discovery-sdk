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

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListListShowBinding;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListController;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListControllerImpl;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.FlexibleAdapterEventListItems;
import com.schedjoules.eventdiscovery.framework.eventlist.flexibleadapter.Copying;
import com.schedjoules.eventdiscovery.framework.eventlist.flexibleadapter.FlexibleAdapterFactory;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EdgeReachScrollListener;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.EventResultPageBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BundleBuilder;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.service.ApiService;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * Fragment for the event item list on the list screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListListShowFragment extends BaseFragment
{
    private FutureServiceConnection<ApiService> mApiService;
    private EventListController mListItemsController;
    private FlexibleAdapter<IFlexible> mAdapter;
    private SchedjoulesFragmentEventListListShowBinding mViews;

    private boolean mIsInitializing = true;


    public static Fragment newInstance(ResultPage<Envelope<Event>> resultPageBox)
    {
        EventListListShowFragment fragment = new EventListListShowFragment();
        fragment.setArguments(new BundleBuilder().with(Keys.EVENTS_RESULT_PAGE, new EventResultPageBox(resultPageBox)).build());
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
        mViews = DataBindingUtil.inflate(inflater, R.layout.schedjoules_fragment_event_list_list_show, container, false);

        initAdapterAndRecyclerView(mIsInitializing);
        if (mIsInitializing)
        {
            mListItemsController.showEvents(new Argument<>(Keys.EVENTS_RESULT_PAGE, this).get());
        }

        mIsInitializing = false;
        return mViews.getRoot();
    }


    private void initAdapterAndRecyclerView(boolean isInitializing)
    {
        Factory<FlexibleAdapter<IFlexible>> adapterFactory = new FlexibleAdapterFactory();
        if (!isInitializing && mAdapter != null)
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

}
