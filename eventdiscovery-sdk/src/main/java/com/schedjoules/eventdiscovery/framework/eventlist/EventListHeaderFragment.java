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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListHeaderBinding;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.locationpicker.LocationPickerPlaceSelection;
import com.schedjoules.eventdiscovery.framework.locationpicker.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.FragmentBuilder;
import com.schedjoules.eventdiscovery.framework.widgets.TextWithIcon;

import org.dmfs.pigeonpost.Cage;


/**
 * Fragment for the header (Toolbar and potentially filters) part of the Event List screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListHeaderFragment extends BaseFragment
{
    private TextView mToolbarTitle;
    private boolean mHasOnActivityResult;


    public static Fragment newInstance(Cage<Boolean> reloadCage)
    {
        return new FragmentBuilder(new EventListHeaderFragment())
                .with(Keys.RELOAD_CAGE, new ParcelableBox<>(reloadCage))
                .build();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        SchedjoulesFragmentEventListHeaderBinding views = DataBindingUtil.inflate(inflater, R.layout.schedjoules_fragment_event_list_header, container, false);
        setupToolbar(views);
        return views.getRoot();
    }


    private void setupToolbar(SchedjoulesFragmentEventListHeaderBinding views)
    {
        Toolbar toolbar = views.schedjoulesEventListToolbar;
        toolbar.setTitle(""); // Need to set it to empty, otherwise the activity label is set automatically

        mToolbarTitle = views.schedjoulesEventListToolbarTitle;
        mToolbarTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new LocationPickerPlaceSelection().start(EventListHeaderFragment.this);
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

            new Argument<>(Keys.RELOAD_CAGE, getArguments()).get().pigeon(true).send(getContext());
        }
    }

}
