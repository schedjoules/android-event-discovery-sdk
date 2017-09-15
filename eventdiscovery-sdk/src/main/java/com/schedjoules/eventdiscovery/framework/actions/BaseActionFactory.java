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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.model.ApiLink;
import com.schedjoules.eventdiscovery.framework.utils.charsequence.CharSequenceFactory;

import org.dmfs.httpessentials.types.Link;


/**
 * A basic {@link ActionFactory}.
 *
 * @author Marten Gajda
 */
public final class BaseActionFactory implements ActionFactory
{

    @Nullable
    @Override
    public Action action(@NonNull final Link actionLink, @NonNull Event event)
    {
        String relType = actionLink.relationTypes().iterator().next();
        switch (relType)
        {
            case ApiLink.Rel.Action.ADD_TO_CALENDAR:
                return new SimpleAction(R.string.schedjoules_action_add_to_calendar, R.drawable.schedjoules_ic_event_black_24dp,
                        new AddToCalendarActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.SHARE:
                return new SimpleAction(R.string.schedjoules_action_share, R.drawable.schedjoules_ic_share_black_24dp,
                        new ShareActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.BROWSE:
                int icon;

                switch (actionLink.title())
                {
                    case "Facebook":
                        icon = R.drawable.schedjoules_ic_facebook_box_24dp;
                        break;
                    case "Meetup":
                        icon = R.drawable.ic_meetup_black_24dp;
                        break;
                    default:
                        icon = R.drawable.schedjoules_ic_open_in_new_24dp;
                }
                return new SimpleAction(
                        new CharSequenceFactory()
                        {
                            @Override
                            public CharSequence create(Context context)
                            {
                                return context.getString(R.string.schedjoules_action_browse, actionLink.title());
                            }
                        },
                        icon,
                        new ViewIntentActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.DIRECTIONS:
                return new SimpleAction(new DirectionsLabelFactory(event.locations()), R.drawable.schedjoules_ic_directions_black_24dp,
                        new ViewIntentActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.INFO:
                return new SimpleAction(R.string.schedjoules_action_info, R.drawable.schedjoules_ic_info_outline_black_24dp,
                        new ViewIntentActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.CALL:
                return new SimpleAction(R.string.schedjoules_action_call, R.drawable.schedjoules_ic_call_black_24dp,
                        new ViewIntentActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.BUY:
                return new SimpleAction(R.string.schedjoules_action_buy, R.drawable.schedjoules_ic_shopping_cart_black_24dp,
                        new ViewIntentActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.REMIND:
                return new SimpleAction(R.string.schedjoules_action_remind, R.drawable.schedjoules_ic_access_alarm_black_24dp,
                        new ViewIntentActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.WATCH:
                return new SimpleAction(R.string.schedjoules_action_watch_live, R.drawable.schedjoules_ic_tv_black_24dp,
                        new ViewIntentActionExecutable(actionLink, event));

            case ApiLink.Rel.Action.INVITE:
                return new SimpleAction(R.string.schedjoules_action_invite, R.drawable.schedjoules_ic_person_add_black_24dp,
                        new ViewIntentActionExecutable(actionLink, event));

            default:
                return null;
        }
    }
}
