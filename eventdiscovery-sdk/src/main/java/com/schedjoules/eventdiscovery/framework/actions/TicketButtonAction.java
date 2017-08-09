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

package com.schedjoules.eventdiscovery.framework.actions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.utils.BookTicketLink;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.optionals.AbstractCachingOptional;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.iterators.Function;
import org.dmfs.optional.Optional;
import org.dmfs.optional.decorators.Mapped;


/**
 * {@link Action} for the Ticket button.
 *
 * @author Gabor Keszthelyi
 */
public final class TicketButtonAction extends AbstractCachingOptional<Action>
{

    public TicketButtonAction(final Iterable<Link> actionLinks, final Event event)
    {
        super(new Factory<Optional<Action>>()
        {
            @Override
            public Optional<Action> create()
            {
                return new Mapped<>(new Function<Link, Action>()
                {
                    @Override
                    public Action apply(Link ticketLink)
                    {
                        return new InternalTicketAction(ticketLink, event);
                    }
                }, new BookTicketLink(actionLinks));
            }
        });
    }


    private static final class InternalTicketAction implements Action
    {
        private final Link mTicketLink;
        private final Event mEvent;


        private InternalTicketAction(Link ticketLink, Event event)
        {
            mTicketLink = ticketLink;
            mEvent = event;
        }


        @NonNull
        @Override
        public CharSequence label(@NonNull Context context)
        {
            return context.getText(R.string.schedjoules_event_details_ticket_button_title);
        }


        @NonNull
        @Override
        public Drawable icon(@NonNull Context context)
        {
            throw new UnsupportedOperationException("Ticket action is used for button, no icon.");
        }


        @NonNull
        @Override
        public ActionExecutable actionExecutable()
        {
            return new ViewIntentActionExecutable(mTicketLink, mEvent);
        }
    }
}
