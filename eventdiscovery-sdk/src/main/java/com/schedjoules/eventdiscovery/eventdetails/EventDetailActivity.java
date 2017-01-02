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

package com.schedjoules.eventdiscovery.eventdetails;

import android.os.Bundle;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.BaseActivity;
import com.schedjoules.eventdiscovery.eventlist.EventListActivity;

import org.dmfs.android.dumbledore.SimpleWizard;
import org.dmfs.android.dumbledore.WizardStep;


/**
 * An activity representing a single Event detail screen. This activity is only used on narrow width devices. On
 * tablet-size devices, Event details are presented side-by-side with a list of items in a {@link EventListActivity}
 * using a {@link EventDetailFragment}.
 *
 * @author Gabor Keszthelyi
 * @author Marten Gajda
 */
public final class EventDetailActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedjoules_activity_frame);

        if (savedInstanceState == null)
        {
            // start the wizard with the initial step
            Bundle nestedExtras = getIntent().getBundleExtra("com.schedjoules.nestedExtras");
            WizardStep initialStep = nestedExtras.getParcelable("WizardStep");
            new SimpleWizard(initialStep, R.id.schedjoules_activity_content).start(this);
        }
    }
}
