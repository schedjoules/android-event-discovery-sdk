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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListBinding;
import com.schedjoules.eventdiscovery.discovery.SimpleCoverageTest;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.common.ExternalUrlFeedbackForm;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListMenu;
import com.schedjoules.eventdiscovery.framework.locationpicker.LocationPickerPlaceSelection;
import com.schedjoules.eventdiscovery.framework.locationpicker.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.utils.InsightsTask;
import com.schedjoules.eventdiscovery.framework.widgets.TextWithIcon;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.SerializableDovecote;


/**
 * Fragment for showing the event discovery list.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListFragment extends BaseFragment implements EventListMenu.Listener
{
    private TextView mToolbarTitle;
    private EventListMenu mMenu;
    private SchedjoulesFragmentEventListBinding mViews;
    private Dovecote<Boolean> mCoverageDoveCote;
    private boolean mHasOnActivityResult;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        new InsightsTask(getActivity()).execute(new Screen(new StringToken("list")));

        if (savedInstanceState == null)
        {
            Bundle args = new FragmentEnvironment<Bundle>(this).microFragment().parameter();
            getChildFragmentManager().beginTransaction().add(R.id.schedjoules_event_list_list_holder,
                    EventListListFragment.newInstance(args)).commit();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mViews = DataBindingUtil.inflate(inflater, R.layout.schedjoules_fragment_event_list, container, false);

        mMenu = new EventListMenu(this);
        setHasOptionsMenu(true);

        setupToolbar(mViews);

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
        toolbar.setContentInsetsAbsolute(res.getDimensionPixelSize(R.dimen.schedjoules_list_item_padding_horizontal), toolbar.getContentInsetRight());

        if (res.getBoolean(R.bool.schedjoules_enableBackArrowOnEventListScreen))
        {
            //noinspection ConstantConditions
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        updateToolbarTitle();
    }


    private void updateToolbarTitle()
    {
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.schedjoules_dropdownArrow, typedValue, true);
        mToolbarTitle.setText(new TextWithIcon(getContext(), new SharedPrefLastSelectedPlace(getContext()).get().namedPlace().name(), typedValue.resourceId));
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
        if (mHasOnActivityResult)
        {
            mHasOnActivityResult = false;
            updateToolbarTitle();
            getChildFragmentManager().beginTransaction().replace(R.id.schedjoules_event_list_list_holder,
                    EventListListFragment.newInstance(getArguments())).commit();
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
        getView().post(new Runnable()
        {
            @Override
            public void run()
            {
                new ExternalUrlFeedbackForm().show(getActivity(),
                        new FragmentEnvironment<>(EventListFragment.this).host());
            }
        });
    }


    @Override
    public void onDestroyView()
    {
        mCoverageDoveCote.dispose();
        super.onDestroyView();
    }

}
