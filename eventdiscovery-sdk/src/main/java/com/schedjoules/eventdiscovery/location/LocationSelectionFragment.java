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

package com.schedjoules.eventdiscovery.location;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.BaseActivity;
import com.schedjoules.eventdiscovery.common.BaseFragment;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentLocationSelectionBinding;


/**
 * Fragment for the location selection.
 *
 * @author Gabor Keszthelyi
 */
public final class LocationSelectionFragment extends BaseFragment
{
    public static Fragment newInstance()
    {
        return new LocationSelectionFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        SchedjoulesFragmentLocationSelectionBinding views = DataBindingUtil.inflate(inflater,
                R.layout.schedjoules_fragment_location_selection, container, false);

        initToolbar(views);

        return views.getRoot();
    }


    private void initToolbar(SchedjoulesFragmentLocationSelectionBinding views)
    {
        BaseActivity activity = (BaseActivity) getActivity();
        Toolbar toolbar = views.schedjoulesLocationSelectionToolbar;

        toolbar.setTitle("Location selection");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
