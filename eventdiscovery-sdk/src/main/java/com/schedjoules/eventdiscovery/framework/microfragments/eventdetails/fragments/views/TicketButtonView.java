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

import android.widget.Button;

import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventDetailsBinding;
import com.schedjoules.eventdiscovery.framework.actions.Action;
import com.schedjoules.eventdiscovery.framework.actions.ActionClickListener;
import com.schedjoules.eventdiscovery.framework.utils.OptionalView;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;

import org.dmfs.optional.Optional;


/**
 * Represents the Book Ticket button on the event details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class TicketButtonView implements SmartView<Optional<Action>>
{
    private final OptionalView mButtonArea;
    private final Button mButton;
    private final OptionalView mSpace;


    public TicketButtonView(SchedjoulesFragmentEventDetailsBinding views)
    {
        mButtonArea = new OptionalView(views.schedjoulesEventDetailsTicketButton.getRoot());
        mButton = views.schedjoulesEventDetailsTicketButton.schedjoulesEventDetailsTicketButton;
        mSpace = new OptionalView(views.schedjoulesEventDetailsTicketButtonSpace);
    }


    @Override
    public void update(Optional<Action> bookAction)
    {
        mButtonArea.update(bookAction);
        mSpace.update(bookAction);

        if (bookAction.isPresent())
        {
            mButton.setOnClickListener(new ActionClickListener(bookAction.value().actionExecutable()));
        }
    }
}
