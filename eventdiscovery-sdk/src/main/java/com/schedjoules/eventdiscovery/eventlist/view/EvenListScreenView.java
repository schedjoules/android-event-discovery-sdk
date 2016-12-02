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

import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.listen.OnCreateOptionsMenu;
import com.schedjoules.eventdiscovery.framework.listen.OnOptionsItemSelected;


/**
 * View component of the Event List screen.
 *
 * @author Gabor Keszthelyi
 */
public interface EvenListScreenView extends OnCreateOptionsMenu, OnOptionsItemSelected
{
    int TABLET_EVENT_DETAIL_CONTAINER = R.id.schedjoules_event_detail_container;

    void init();

    void setToolbarTitle(CharSequence title);

    void setAdapter(RecyclerView.Adapter adapter);

    void setBottomReachScrollListener(BottomReachScrollListener.Listener listener);

    void setUserActionListener(UserActionListener userActionListener);

    interface UserActionListener
    {
        void onToolbarTitleClick();

        void onUpButtonClick();

        void onLocationMenuIconClick();
    }
}
