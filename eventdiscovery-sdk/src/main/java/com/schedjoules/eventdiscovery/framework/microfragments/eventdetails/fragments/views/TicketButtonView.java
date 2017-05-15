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

import android.content.Context;
import android.widget.Button;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventDetailsBinding;
import com.schedjoules.eventdiscovery.framework.actions.ActionClickListener;
import com.schedjoules.eventdiscovery.framework.actions.ViewIntentActionExecutable;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ShowEventMicroFragment;
import com.schedjoules.eventdiscovery.framework.model.BasicTicketInfo;
import com.schedjoules.eventdiscovery.framework.model.TicketInfo;
import com.schedjoules.eventdiscovery.framework.utils.BookTicketLink;
import com.schedjoules.eventdiscovery.framework.utils.ButtonCompat;
import com.schedjoules.eventdiscovery.framework.utils.OptionalView;
import com.schedjoules.eventdiscovery.framework.utils.colors.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.Optional;


/**
 * Represents the Book Ticket button on the event details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class TicketButtonView implements SmartView<ShowEventMicroFragment.EventParams>
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
    public void update(ShowEventMicroFragment.EventParams eventParams)
    {
        Optional<Link> bookTicketLink = new BookTicketLink(eventParams.actions());

        mButtonArea.update(bookTicketLink);
        mSpace.update(bookTicketLink);

        if (bookTicketLink.isPresent())
        {
            mButton.setOnClickListener(
                    new ActionClickListener(new ViewIntentActionExecutable(bookTicketLink.value(), eventParams.event())));

            TicketInfo ticketInfo = new BasicTicketInfo(bookTicketLink.value());

            Context context = mButton.getContext();
            mButton.setText(ticketInfo.isAvailable() ?
                    context.getText(R.string.schedjoules_event_details_ticket_button_title) :
                    context.getText(R.string.schedjoules_event_details_ticket_button_title_not_available));

            if (!ticketInfo.isAvailable())
            {
                ButtonCompat.setBackgroundTint(mButton,
                        new AttributeColor(context, R.attr.schedjoules_disabledButtonColor));
            }
        }
    }
}
