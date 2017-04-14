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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.views;

import android.widget.TextView;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesDetailsHeaderBinding;
import com.schedjoules.eventdiscovery.framework.datetime.LongDateAndTimeNoYear;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * Represents the date-time info displaying view on the event details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventHeaderDateTimeView implements SmartView<Event>
{
    private final TextView mDateTimeViewExpanded;
    private final TextView mDateTimeViewCollapsed;


    public EventHeaderDateTimeView(SchedjoulesDetailsHeaderBinding binding)
    {
        mDateTimeViewExpanded = binding.datetimeExpanded;
        mDateTimeViewCollapsed = binding.datetimeCollapsed;
    }


    @Override
    public void update(Event event)
    {
        CharSequence rangeCharSequence = new LongDateAndTimeNoYear(event.start()).value(mDateTimeViewCollapsed.getContext());
        // update the dark and the light version of the date
        mDateTimeViewExpanded.setText(rangeCharSequence);
        mDateTimeViewCollapsed.setText(rangeCharSequence);
    }
}
