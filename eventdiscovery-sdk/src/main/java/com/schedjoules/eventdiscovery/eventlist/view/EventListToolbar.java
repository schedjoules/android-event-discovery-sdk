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
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.BaseActivity;


/**
 * The toolbar of the Event list screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListToolbar
{
    private final Toolbar mToolbar;
    private final Listener mListener;


    public EventListToolbar(Toolbar toolbar, Listener listener)
    {
        mToolbar = toolbar;
        mListener = listener;
    }


    public void initToolbar(FragmentActivity fragmentActivity)
    {
        BaseActivity activity = (BaseActivity) fragmentActivity;

        mToolbar.setTitle(""); // Need to set it to something here, otherwise it will use title from Activity label
        mToolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mListener.onToolbarTitleClick();
            }
        });
        activity.setSupportActionBar(mToolbar);

        Resources res = activity.getResources();
        if (res.getBoolean(R.bool.schedjoules_enableBackArrowOnEventListScreen))
        {
            //noinspection ConstantConditions
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else
        {
            mToolbar.setTitleMarginStart(res.getDimensionPixelSize(R.dimen.schedjoules_list_item_horizontal_margin));
        }
    }


    public void setToolbarTitle(CharSequence title)
    {
        mToolbar.setTitle(title);
    }


    public interface Listener
    {
        void onToolbarTitleClick();
    }

}
