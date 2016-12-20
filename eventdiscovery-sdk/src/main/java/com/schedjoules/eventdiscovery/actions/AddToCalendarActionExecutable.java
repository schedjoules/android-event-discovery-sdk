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

package com.schedjoules.eventdiscovery.actions;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.insights.steps.ActionInteraction;
import com.schedjoules.eventdiscovery.EventIntents;
import com.schedjoules.eventdiscovery.utils.InsightsTask;

import org.dmfs.httpessentials.types.Link;

import static com.schedjoules.eventdiscovery.utils.LocationFormatter.longLocationFormat;


/**
 * {@link ActionExecutable} that sends an ACTION_SEND Intent with text info from the event:
 * <p>
 * <localized date+time> - <title>
 *
 * @author Gabor Keszthelyi
 */
public final class AddToCalendarActionExecutable implements ActionExecutable
{

    private final Link mLink;
    private final Event mEvent;


    public AddToCalendarActionExecutable(@NonNull Link link, @NonNull Event event)
    {
        mLink = link;
        mEvent = event;
    }


    @Override
    public void execute(@NonNull Activity activity)
    {
        new InsightsTask(activity).execute(new ActionInteraction(mLink, mEvent));

        Intent addIntent = new Intent(EventIntents.ACTION_ADD_TO_CALENDAR);
        addIntent.putExtras(extrasForAddToCalendar());
        addIntent.setPackage(activity.getPackageName());
        activity.startActivity(addIntent);
    }


    private Bundle extrasForAddToCalendar()
    {
        Bundle bundle = new Bundle(8);
        bundle.putLong(CalendarContract.EXTRA_EVENT_BEGIN_TIME, mEvent.start().getTimestamp());
        bundle.putLong(CalendarContract.EXTRA_EVENT_END_TIME,
                mEvent.start().addDuration(mEvent.duration()).getTimestamp());
        bundle.putBoolean(CalendarContract.EXTRA_EVENT_ALL_DAY, mEvent.start().isAllDay());
        bundle.putString(CalendarContract.Events.TITLE, mEvent.title());
        bundle.putString(CalendarContract.Events.DESCRIPTION, mEvent.description());
        bundle.putString(CalendarContract.Events.EVENT_LOCATION, longLocationFormat(mEvent.locations()));
        bundle.putString(EventIntents.EXTRA_SCHEDJOULES_EVENT_UID, mEvent.uid());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            bundle.putString(CalendarContract.Events.UID_2445, mEvent.uid());
        }

        return bundle;
    }

}
