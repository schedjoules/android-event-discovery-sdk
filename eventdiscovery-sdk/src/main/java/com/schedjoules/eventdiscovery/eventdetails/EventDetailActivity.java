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

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.queries.EventByUid;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.ErrorFragment;
import com.schedjoules.eventdiscovery.common.LoaderFragment;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesActivityEventDetailBinding;
import com.schedjoules.eventdiscovery.eventlist.EventListActivity;
import com.schedjoules.eventdiscovery.framework.access.Fragments;
import com.schedjoules.eventdiscovery.model.ParcelableEvent;
import com.schedjoules.eventdiscovery.model.SchedJoulesLinks;
import com.schedjoules.eventdiscovery.service.ApiService;
import com.schedjoules.eventdiscovery.service.ServiceJob;
import com.schedjoules.eventdiscovery.service.SimpleServiceJobQueue;
import com.schedjoules.eventdiscovery.utils.BaseActivity;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.StringToken;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * An activity representing a single Event detail screen. This activity is only used on narrow width devices. On tablet-size devices, Event details are
 * presented side-by-side with a list of items in a {@link EventListActivity} using a {@link EventDetailFragment}.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailActivity extends BaseActivity
{
    public final static String EXTRA_CUSTOM_PARCELABLES = "com.schedjoules.CUSTOM_PARCELABLES";
    public final static String CUSTOM_EXTRA_EVENT = "event";
    public final static String EXTRA_EVENT_UID = "com.schedjoules.event_uid";

    private final static String STATE_EVENT = "com.schedjoules.event";

    private SchedjoulesActivityEventDetailBinding mViews;
    private FutureServiceConnection<ApiService> mApiServiceConnection;
    private ParcelableEvent mEvent;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        initView();

        if (savedInstanceState == null)
        {
            if (intent.hasExtra(EXTRA_CUSTOM_PARCELABLES))
            {
                mEvent = intent.getBundleExtra(EXTRA_CUSTOM_PARCELABLES).getParcelable(CUSTOM_EXTRA_EVENT);
                new Fragments(this).add(R.id.schedjoules_event_detail_container, EventDetailFragment.newInstance(mEvent));
            }
            else
            {
                mApiServiceConnection = new ApiService.FutureConnection(this);
                new SimpleServiceJobQueue<>(mApiServiceConnection).post(new ServiceJob<ApiService>()
                {
                    @Override
                    public void execute(ApiService service)
                    {
                        try
                        {
                            mEvent = new ParcelableEvent(
                                    service.apiResponse(new EventByUid(new StringToken(intent.getStringExtra(EXTRA_EVENT_UID)))).payload());
                            new Fragments(EventDetailActivity.this).replace(R.id.schedjoules_event_detail_container, EventDetailFragment.newInstance(mEvent));
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    showEventDetailsOnToolbar(mEvent);
                                }
                            });
                        }
                        catch (URISyntaxException | ProtocolError | ProtocolException | IOException | RuntimeException e)
                        {
                            showError();
                        }
                    }


                    @Override
                    public void onTimeOut()
                    {
                        showError();
                    }
                }, 5000);
                // show an in-progress fragment
                new Fragments(this).add(R.id.schedjoules_event_detail_container, new LoaderFragment());
            }
        }
        else
        {
            mEvent = intent.getParcelableExtra(STATE_EVENT);
        }

        showEventDetailsOnToolbar(mEvent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_EVENT, mEvent);
    }


    @Override
    protected void onDestroy()
    {
        if (mApiServiceConnection != null && mApiServiceConnection.isConnected())
        {
            mApiServiceConnection.disconnect();
        }
        super.onDestroy();
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

        mViews.schedjoulesEventDetailToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.schedjoules_colorPrimary));
    }


    private void showError()
    {
        new Fragments(this).replace(R.id.schedjoules_event_detail_container, new ErrorFragment());
    }


    private void showEventDetailsOnToolbar(Event event)
    {
        if (event == null)
        {
            return;
        }
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
