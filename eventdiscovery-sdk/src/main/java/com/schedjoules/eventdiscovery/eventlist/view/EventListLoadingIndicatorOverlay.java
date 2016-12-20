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
import android.widget.ProgressBar;


/**
 * Represents the loading indicator overlay on the event list.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListLoadingIndicatorOverlay
{
    private final ProgressBar mProgressBar;


    public EventListLoadingIndicatorOverlay(ProgressBar progressBar)
    {
        mProgressBar = progressBar;
    }


    public void show()
    {
        mProgressBar.setVisibility(View.VISIBLE);
    }


    public void hide()
    {
        mProgressBar.setVisibility(View.GONE);
    }
}
