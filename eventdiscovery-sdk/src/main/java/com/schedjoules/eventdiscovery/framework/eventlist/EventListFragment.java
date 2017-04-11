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

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.schedjoules.eventdiscovery.discovery.SimpleCoverageTest;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.common.ExternalUrlFeedbackForm;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListController;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListControllerImpl;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.FlexibleAdapterEventListItems;
import com.schedjoules.eventdiscovery.framework.eventlist.flexibleadapter.Copying;
import com.schedjoules.eventdiscovery.framework.eventlist.flexibleadapter.FlexibleAdapterFactory;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EdgeReachScrollListener;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListMenu;
import com.schedjoules.eventdiscovery.framework.location.LocationPickerPlaceSelection;
import com.schedjoules.eventdiscovery.framework.location.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.framework.utils.InsightsTask;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.widgets.TextWithIcon;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.SerializableDovecote;
import org.dmfs.rfc5545.DateTime;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

import static com.schedjoules.eventdiscovery.framework.EventIntents.EXTRA_GEOLOCATION;
import static com.schedjoules.eventdiscovery.framework.EventIntents.EXTRA_START_AFTER_TIMESTAMP;


/**
 * Fragment for showing the event discovery list.
 *
 * @author Gabor Keszthelyi
 */

public final class EventListFragment extends BaseFragment implements EventListMenu.Listener
{
    private FutureServiceConnection<ApiService> mApiService;
    private EventListController mListItemsController;

    private TextView mToolbarTitle;
    private EventListMenu mMenu;

    private FlexibleAdapter<IFlexible> mAdapter;
    private SchedjoulesFragmentEventListBinding mViews;

    private Dovecote<Boolean> mCoverageDoveCote;

    private boolean mInitialized;
    private boolean mRestored;
    private boolean mHasOnActivityResult;


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

        mApiService = new ApiService.FutureConnection(getActivity());

        mListItemsController = new EventListControllerImpl(mApiService, new FlexibleAdapterEventListItems());

        new InsightsTask(getActivity()).execute(new Screen(new StringToken("list")));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mInitialized = false;
        mRestored = savedInstanceState != null;

        mViews = DataBindingUtil.inflate(inflater,
                R.layout.schedjoules_fragment_event_list, container, false);

        mMenu = new EventListMenu(this);
        setHasOptionsMenu(true);

        setupToolbar(mViews);

        mListItemsController.setBackgroundMessageUI(
                new EventListBackgroundMessage(mViews.schedjoulesEventListBackgroundMessage));
        mListItemsController.setLoadingIndicatorUI(
                new EventListLoadingIndicatorOverlay(mViews.schedjoulesEventListProgressBar));

        mCoverageDoveCote = new SerializableDovecote<>(getActivity(), "coveragetest", new Dovecote.OnPigeonReturnCallback<Boolean>()
        {
            @Override
            public void onPigeonReturn(@NonNull Boolean serializable)
            {
                if (!serializable)
                {
                    Snackbar.make(mViews.getRoot(), R.string.schedjoules_message_country_not_supported, Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        });
        new SimpleCoverageTest(mCoverageDoveCote).execute(getActivity());

        if (Build.VERSION.SDK_INT < 21)
        {
            // on older Version the progressbar won't pick up the accent color, so we have to set it manually
            mViews.schedjoulesEventListProgressBar.getIndeterminateDrawable()
                    .setColorFilter(getResources().getColor(R.color.schedjoules_colorAccent),
                            PorterDuff.Mode.MULTIPLY);
        }
        return mViews.getRoot();
    }


    private void setupToolbar(SchedjoulesFragmentEventListBinding views)
    {
        Toolbar toolbar = views.schedjoulesEventListToolbar;
        toolbar.setTitle(""); // Need to set it to empty, otherwise the activity label is set automatically

        mToolbarTitle = views.schedjoulesEventListToolbarTitle;
        mToolbarTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new LocationPickerPlaceSelection().start(EventListFragment.this);
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


    @Override
    public void onFeedbackMenuClick()
    {
        new ExternalUrlFeedbackForm().show(getActivity());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mHasOnActivityResult = resultCode == Activity.RESULT_OK;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        // Log.d("OnResumeStates", String.format("Initialized = %s | Restored = %s | HasActivityResult = %s", mInitialized, mRestored, mHasOnActivityResult));

        if ((!mInitialized && !mRestored) || mHasOnActivityResult)
        {
            mInitialized = true;
            mHasOnActivityResult = false;
            update(true);
        }
        else if (!mInitialized && mRestored)
        {
            mInitialized = true;
            update(false);
        }
    }


    private void update(boolean freshList)
    {
        // Log.d("OnResumeStates", freshList ? "ClearingUpdate" : "NonClearingUpdate");
        initAdapterAndRecyclerView(freshList);
        mToolbarTitle.setText(
                new TextWithIcon(getContext(), new SharedPrefLastSelectedPlace(getContext()).get().namedPlace().name(),
                        R.drawable.schedjoules_ic_arrow_drop_down));
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

        RecyclerView recyclerView = mViews.schedjoulesEventListInclude.schedjoulesEventList;
        recyclerView.setAdapter(adapter);
        adapter.setStickyHeaders(true); // Better to set it after adapter has been set to RecyclerView
        recyclerView.addOnScrollListener(
                new EdgeReachScrollListener(recyclerView, mListItemsController,
                        EventListControllerImpl.CLOSE_TO_TOP_OR_BOTTOM_THRESHOLD));

        mListItemsController.setAdapter(adapter);
        mAdapter = adapter;
    }


    @Override
    public void onDestroyView()
    {
        mCoverageDoveCote.dispose();
        super.onDestroyView();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mApiService.disconnect();
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
            return new SharedPrefLastSelectedPlace(getContext()).get().geoLocation();
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
