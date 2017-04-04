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
import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewEventDetailsDatetimeBinding;
import com.schedjoules.eventdiscovery.framework.datetime.LongDate;
import com.schedjoules.eventdiscovery.framework.datetime.StartAndEndTime;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * Represents the date-time info displaying view on the event details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsDateTimeView implements SmartView<Event>
{
    private final TextView mDateView;
    private final TextView mTimeView;


    public EventDetailsDateTimeView(SchedjoulesViewEventDetailsDatetimeBinding binding)
    {
        mDateView = binding.schedjoulesEventDetailsDate;
        mTimeView = binding.schedjoulesEventDetailsTime;
    }


    @Override
    public void update(Event event)
    {
        mDateView.setText(new LongDate(event.start()).value(mDateView.getContext()));
        mTimeView.setText(new StartAndEndTime(event).value(mTimeView.getContext()));
    }
}
