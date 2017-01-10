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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.menu;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.listen.OnCreateOptionsMenuFragment;
import com.schedjoules.eventdiscovery.framework.listen.OnOptionsItemSelected;


/**
 * Options menu for the Event details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsMenu implements OnCreateOptionsMenuFragment, OnOptionsItemSelected
{

    private final EventDetailsMenu.Listener mListener;


    public EventDetailsMenu(EventDetailsMenu.Listener listener)
    {
        mListener = listener;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.schedjoules_event_details_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.schedjoules_event_details_menu_feedback)
        {
            mListener.onFeedbackMenuClick();
            return true;
        }
        return false;
    }


    public interface Listener
    {
        void onFeedbackMenuClick();
    }
}
