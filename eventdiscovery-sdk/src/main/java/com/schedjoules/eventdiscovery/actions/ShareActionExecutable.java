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

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.insights.steps.ActionInteraction;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.utils.DateTimeFormatter;
import com.schedjoules.eventdiscovery.utils.InsightsTask;

import org.dmfs.httpessentials.types.Link;

import java.net.URI;


/**
 * {@link ActionExecutable} that sends an ACTION_SEND Intent with text info from the event:
 * <p>
 * <localized date+time> - <title>
 *
 * @author Gabor Keszthelyi
 */
public final class ShareActionExecutable implements ActionExecutable
{
    private final Link mLink;
    private final Event mEvent;


    public ShareActionExecutable(@NonNull Link link, @NonNull Event event)
    {
        mLink = link;
        mEvent = event;
    }


    @Override
    public void execute(@NonNull Context context)
    {
        new InsightsTask(context).execute(new ActionInteraction(mLink, mEvent));

        String eventText = eventText(context);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, eventText);
        sendIntent.setType("text/plain");

        if (sendIntent.resolveActivity(context.getPackageManager()) != null)
        {
            context.startActivity(sendIntent);
        }
        else
        {
            Toast.makeText(context, R.string.schedjoules_action_cannot_handle_message, Toast.LENGTH_LONG).show();
        }
    }


    @NonNull
    private String eventText(@NonNull Context context)
    {
        URI uri = mLink.target();
        return String.format(uri == null ? "%s - %s" : "%s - %s %s",
                DateTimeFormatter.longEventStartDateTime(context, mEvent),
                mEvent.title(), uri);
    }
}
