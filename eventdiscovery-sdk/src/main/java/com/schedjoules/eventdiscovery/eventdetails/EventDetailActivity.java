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

package com.schedjoules.eventdiscovery.eventdetails;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.utils.BaseActivity;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesActivityEventDetailBinding;
import com.schedjoules.eventdiscovery.eventlist.EventListActivity;
import com.schedjoules.eventdiscovery.framework.access.Fragments;
import com.schedjoules.eventdiscovery.model.ParcelableEvent;
import com.schedjoules.eventdiscovery.model.SchedJoulesLinks;


/**
 * An activity representing a single Event detail screen. This activity is only used on narrow width devices. On tablet-size devices, Event details are
 * presented side-by-side with a list of items in a {@link EventListActivity} using a {@link EventDetailFragment}.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailActivity extends BaseActivity
{
    private final static String EXTRA_CUSTOM_PARCELABLES = "com.schedjoules.CUSTOM_PARCELABLES";
    private static final String EXTRA_EVENT = "event";

    private SchedjoulesActivityEventDetailBinding mViews;


    public static Intent launchIntent(Context context, Event event)
    {
        Intent intent = new Intent(context, EventDetailActivity.class);
        Bundle nestedBundle = new Bundle();
        nestedBundle.putParcelable(EXTRA_EVENT, new ParcelableEvent(event));
        intent.putExtra(EXTRA_CUSTOM_PARCELABLES, nestedBundle);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Event event = getIntent().getBundleExtra(EXTRA_CUSTOM_PARCELABLES).getParcelable(EXTRA_EVENT);

        initView();
        showEventDetailsOnToolbar(event);

        if (savedInstanceState == null)
        {
            new Fragments(this).add(R.id.schedjoules_event_detail_container, EventDetailFragment.newInstance(event));
        }
    }


    private void initView()
    {
        mViews = DataBindingUtil.setContentView(this, R.layout.schedjoules_activity_event_detail);
        setSupportActionBar(mViews.schedjoulesEventDetailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mViews.schedjoulesEventDetailToolbarLayout.setContentScrimColor(
                ContextCompat.getColor(this, R.color.schedjoules_colorPrimary));
    }


    private void showEventDetailsOnToolbar(Event event)
    {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(event.title());

        Glide.with(this)
                .load(new SchedJoulesLinks(event.links()).bannerUri())
                .into(mViews.schedjoulesEventDetailBanner);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
