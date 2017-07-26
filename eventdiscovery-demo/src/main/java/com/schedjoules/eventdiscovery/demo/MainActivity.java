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

package com.schedjoules.eventdiscovery.demo;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.schedjoules.client.insights.steps.EntryPoint;
import com.schedjoules.eventdiscovery.demo.optionals.AddToCalendarActivity;
import com.schedjoules.eventdiscovery.discovery.BasicEventDiscovery;
import com.schedjoules.eventdiscovery.eventdetails.UidEventDetails;
import com.schedjoules.eventdiscovery.framework.utils.InsightsTask;

import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;


/**
 * Main Activity for a demo app that uses the SchedJoules Event Discover SDK.
 *
 * @author Gabor Keszthelyi
 */
public final class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Re-registering AddToCalendarActivity in case it had been disabled with the button click on that screen
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, AddToCalendarActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        if (savedInstanceState == null)
        {
            // first launch send insight about presentation of entry point
            new InsightsTask(this).execute(new EntryPoint(new StringToken("MainActivity")));
        }
    }


    public void onDiscoverNow(View view)
    {
        new BasicEventDiscovery().start(this);
    }


    public void onDiscoverLater(View view)
    {
        new BasicEventDiscovery()
                .withStart(DateTime.now().addDuration(new Duration(1, 10, 0)))
                .withTheme(R.style.SchedJoules_Theme_Dark)
                .start(this);
    }


    public void onEventDetailsByUid(View view)
    {
        new UidEventDetails("091e93592f7571c0").show(this);
    }
}
