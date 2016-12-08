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

package com.schedjoules.eventdiscovery.eventlist.view;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesActivityEventListBinding;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsProviderImpl;
import com.schedjoules.eventdiscovery.utils.BaseActivity;


/**
 * Implementation for {@link EvenListScreenView}.
 *
 * @author Gabor Keszthelyi
 */
public final class EvenListScreenViewImpl implements EvenListScreenView
{
    private final BaseActivity mActivity;

    private SchedjoulesActivityEventListBinding mViews;
    private Toolbar mToolbar;
    private UserActionListener mUserActionListener;
    private RecyclerView mRecyclerView;


    public EvenListScreenViewImpl(BaseActivity activity)
    {
        mActivity = activity;
    }


    @Override
    public void init()
    {
        mViews = DataBindingUtil.setContentView(mActivity, R.layout.schedjoules_activity_event_list);

        initToolbar();

        // TODO These divider appear around date header items as well, remove them from there
        // TODO Check FlexibleAdapter, it might have solution out of the box
        mRecyclerView = mViews.schedjoulesEventListInclude.schedjoulesEventList;
    }


    private void initToolbar()
    {
        mToolbar = mViews.schedjoulesEventListToolbar;
        mToolbar.setTitle(""); // Need to set it to something here, otherwise it will use title from Activity label
        mToolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mUserActionListener.onToolbarTitleClick();
            }
        });
        mActivity.setSupportActionBar(mToolbar);

        Resources res = mActivity.getResources();
        if (res.getBoolean(R.bool.schedjoules_enableBackArrowOnEventListScreen))
        {
            //noinspection ConstantConditions
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else
        {
            mToolbar.setTitleMarginStart(res.getDimensionPixelSize(R.dimen.schedjoules_list_item_horizontal_margin));
        }
    }


    @Override
    public void setAdapter(RecyclerView.Adapter adapter)
    {
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void setEdgeReachScrollListener(EdgeReachScrollListener.Listener listener)
    {
        EdgeReachScrollListener scrollListener = new EdgeReachScrollListener(mRecyclerView, listener,
                EventListItemsProviderImpl.CLOSE_TO_TOP_OR_BOTTOM_THRESHOLD);
        mRecyclerView.addOnScrollListener(scrollListener);
    }


    @Override
    public void setUserActionListener(UserActionListener userActionListener)
    {
        mUserActionListener = userActionListener;
    }


    @Override
    public void setToolbarTitle(CharSequence title)
    {
        mToolbar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        mActivity.getMenuInflater().inflate(R.menu.schedjoules_event_list_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            mUserActionListener.onUpButtonClick();
            return true;
        }
        else if (item.getItemId() == R.id.schedjoules_event_list_menu_location)
        {
            mUserActionListener.onLocationMenuIconClick();
            return true;
        }
        return false;
    }
}
