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

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventDetailsBinding;
import com.schedjoules.eventdiscovery.framework.actions.Action;
import com.schedjoules.eventdiscovery.framework.actions.ActionLinkRelTypes;
import com.schedjoules.eventdiscovery.framework.actions.BaseActionFactory;
import com.schedjoules.eventdiscovery.framework.actions.OptionalAction;
import com.schedjoules.eventdiscovery.framework.actions.TicketButtonAction;
import com.schedjoules.eventdiscovery.framework.location.model.VenueName;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ShowEventMicroFragment;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.NullSafe;
import org.dmfs.optional.Optional;

import java.util.List;


/**
 * Represents the event details screen view.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsView implements SmartView<ShowEventMicroFragment.EventParams>
{
    private final SchedjoulesFragmentEventDetailsBinding mViews;


    public EventDetailsView(SchedjoulesFragmentEventDetailsBinding views)
    {
        mViews = views;
    }


    @Override
    public void update(ShowEventMicroFragment.EventParams eventParams)
    {
        List<Link> actionLinks = eventParams.actions();
        Event event = eventParams.event();

        new VenueNameView(mViews.schedjoulesEventDetailsVenueName).update(new VenueName(event.locations()));

        new EventDescriptionView(mViews.schedjoulesEventDetailsDescription).update(new NullSafe<>(event.description()));

        BaseActionFactory actionFactory = new BaseActionFactory();

        new ActionView(mViews.schedjoulesEventDetailsActionShare)
                .update(new OptionalAction(ActionLinkRelTypes.SHARE, actionLinks, actionFactory, event));

        new ActionView(mViews.schedjoulesEventDetailsActionAddToCalendar)
                .update(new OptionalAction(ActionLinkRelTypes.ADD_TO_CALENDAR, actionLinks, actionFactory, event));

        Optional<Action> directionsAction = new OptionalAction(ActionLinkRelTypes.DIRECTIONS, actionLinks, actionFactory, event);
//        new OptionalView(mViews.schedjoulesEventDetailsAddressDivider).update(directionsAction);
        new ActionView(mViews.schedjoulesEventDetailsActionDirections).update(directionsAction);

        new TicketButtonView(mViews).update(new TicketButtonAction(actionLinks, event));
    }
}
