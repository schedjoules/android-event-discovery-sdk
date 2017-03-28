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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;

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
    public Action action(@NonNull Link actionLink, @NonNull Event event)
    {
        String relType = actionLink.relationTypes().iterator().next();
        switch (relType)
        {
            // Not shown as Action currently but may be useful later:
//            case "http://schedjoules.com/rel/action/book":
//                return new BookAction(actionLink, event);

            case "http://schedjoules.com/rel/action/add-to-calendar":
                return new SimpleAction(R.string.schedjoules_action_add_to_calendar, R.drawable.schedjoules_ic_action_add_to_calendar,
                        new AddToCalendarActionExecutable(actionLink, event));

            case "http://schedjoules.com/rel/action/share":
                return new SimpleAction(R.string.schedjoules_action_share, R.drawable.schedjoules_ic_action_share,
                        new ShareActionExecutable(actionLink, event));

            case "http://schedjoules.com/rel/action/directions":
                return new SimpleAction(new DirectionsLabelFactory(event.locations()), R.drawable.schedjoules_ic_action_directions,
                        new ViewIntentActionExecutable(actionLink, event));

            case "http://schedjoules.com/rel/action/info":
                return new SimpleAction(R.string.schedjoules_action_info, R.drawable.schedjoules_ic_action_info,
                        new ViewIntentActionExecutable(actionLink, event));

            case "http://schedjoules.com/rel/action/call":
                return new SimpleAction(R.string.schedjoules_action_call, R.drawable.schedjoules_ic_action_call,
                        new ViewIntentActionExecutable(actionLink, event));

            case "http://schedjoules.com/rel/action/buy":
                return new SimpleAction(R.string.schedjoules_action_buy, R.drawable.schedjoules_ic_action_buy,
                        new ViewIntentActionExecutable(actionLink, event));

            case "http://schedjoules.com/rel/action/remind":
                return new SimpleAction(R.string.schedjoules_action_remind, R.drawable.schedjoules_ic_action_remind,
                        new ViewIntentActionExecutable(actionLink, event));

            case "http://schedjoules.com/rel/action/watch":
                return new SimpleAction(R.string.schedjoules_action_watch_live, R.drawable.schedjoules_ic_action_watch_live,
                        new ViewIntentActionExecutable(actionLink, event));

            case "http://schedjoules.com/rel/action/invite":
                return new SimpleAction(R.string.schedjoules_action_invite, R.drawable.schedjoules_ic_action_invite,
                        new ViewIntentActionExecutable(actionLink, event));

            default:
                return null;
        }
    }
}
