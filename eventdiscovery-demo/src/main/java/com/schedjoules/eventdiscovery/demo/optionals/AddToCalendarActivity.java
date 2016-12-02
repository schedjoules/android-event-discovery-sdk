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

package com.schedjoules.eventdiscovery.demo.optionals;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.schedjoules.eventdiscovery.EventIntents;
import com.schedjoules.eventdiscovery.demo.R;


/**
 * Activity to demonstrate the option to direct add to calendar intents directly to your activity.
 *
 * @author Gabor Keszthelyi
 */
public final class AddToCalendarActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_calendar);

        String description = String.format(
                "This is an Activity with intent-filter for the specific action:\n'%s'\n\n" +
                        "You can add the filter to your Activity to avoid showing the chooser to the user.\n\n" +
                        "If no Activity with this action is found, then the standard '%s' action is used to fire the intent.",
                EventIntents.ACTION_ADD_TO_CALENDAR, Intent.ACTION_INSERT);

        ((TextView) findViewById(R.id.activity_add_to_calendar_text)).setText(description);
    }


    public void onButtonClick(View view)
    {
        // Disabling this Activity:
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, AddToCalendarActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Toast.makeText(this, "Disabled", Toast.LENGTH_LONG).show();
    }
}
