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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventDetailsBinding;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.common.ExternalUrlFeedbackForm;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ShowEventMicroFragment;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.menu.EventDetailsMenu;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.views.EventDetailsView;
import com.schedjoules.eventdiscovery.framework.utils.InsightsTask;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.httpessentials.types.StringToken;


/**
 * A fragment representing a single Event detail screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailFragment extends BaseFragment implements EventDetailsMenu.Listener
{
    private EventDetailsMenu mMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ShowEventMicroFragment.EventParams parameters = new FragmentEnvironment<ShowEventMicroFragment.EventParams>(this).microFragment().parameters();

        if (savedInstanceState == null)
        {
            new InsightsTask(getActivity()).execute(new Screen(new StringToken("details"), parameters.event()));
        }

        SchedjoulesFragmentEventDetailsBinding views = DataBindingUtil.inflate(inflater, R.layout.schedjoules_fragment_event_details, container, false);

        ((BaseActivity) getActivity()).setSupportActionBar(views.schedjoulesDetailsHeader.schedjoulesEventDetailToolbar);
        setHasOptionsMenu(true);

        mMenu = new EventDetailsMenu(this);

        new EventDetailsView(views).update(parameters);

        return views.getRoot();
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
    public void onFeedbackMenuClick()
    {
        getView().post(new Runnable()
        {
            @Override
            public void run()
            {
                new ExternalUrlFeedbackForm().show(getActivity(),
                        new FragmentEnvironment<>(EventDetailFragment.this).host());
            }
        });
    }
}
