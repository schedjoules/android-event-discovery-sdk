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

import android.view.View;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.utils.BaseActivity;
import com.schedjoules.eventdiscovery.R;


/**
 * Represents the background message of the event list that can be shown when no events have been loaded.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListBackgroundMessage
{
    private final TextView mTextView;


    public EventListBackgroundMessage(BaseActivity activity)
    {
        mTextView = (TextView) activity.findViewById(R.id.schedjoules_event_list_background_message);
    }


    public void showNoEventsFoundMsg()
    {
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(R.string.schedjoules_event_list_no_events_found);
    }


    public void showErrorMsg()
    {
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(R.string.schedjoules_event_list_error_bg_msg);
    }


    public void hide()
    {
        mTextView.setVisibility(View.GONE);
    }


    public void setOnClickListener(final OnClickListener onClickListener)
    {
        mTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickListener.onBackgroundMessageClick();
            }
        });
    }


    public interface OnClickListener
    {
        void onBackgroundMessageClick();
    }

}
