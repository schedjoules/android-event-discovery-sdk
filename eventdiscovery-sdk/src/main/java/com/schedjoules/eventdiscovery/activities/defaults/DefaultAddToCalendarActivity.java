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

package com.schedjoules.eventdiscovery.activities.defaults;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.schedjoules.eventdiscovery.R;


/**
 * Invisible default {@link Activity} to catch {@code schedjoules.event.intent.action.ADD_TO_CALENDAR} if the current app doesn't. It forwards the data to a
 * general "insert event" intent.
 *
 * @author Marten Gajda
 */
public final class DefaultAddToCalendarActivity extends Activity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            startActivity(new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)
                    .putExtras(getIntent().getExtras())
                    .setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT));
            finish();
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(this, R.string.schedjoules_action_cannot_handle_message, Toast.LENGTH_LONG).show();
        }
    }
}
