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

package com.schedjoules.eventdiscovery.framework.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;


/**
 * Fragment for the filter on the list screen.
 *
 * @author Gabor Keszthelyi
 */
// TODO Bug: KitKat dark theme item highlighting doesn't work (works for the title and works for the items on light theme)
// TODO Align Category down arrow to center
// TODO Create up arrow and use it for Category when extended and for CLEAR
// TODO Highlight Category with grey when expanded but not selected (tip: update with Color, not boolean)
// TODO Ripple to change to full item flash as on KitKat?
// TODO Bug: double tap needed to deselect after rotation (1. open, select, keep open 2. rotate 3. open deselect)
// TODO Landscape mode not all items are visible

// TODO Check rotation after real data is wired in
public final class FilterFragment extends BaseFragment
{

    public static Fragment newInstance()
    {
        return new FilterFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.schedjoules_fragment_event_list_filter, container, false);
    }

}
