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

package com.schedjoules.eventdiscovery.framework.activities;

import android.os.Bundle;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.SimpleMicroFragmentFlow;


/**
 * An activity presenting the given {@link MicroFragment}.
 *
 * @author Marten Gajda
 */
public final class MicroFragmentHostActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedjoules_activity_microfragment_host);

        if (savedInstanceState == null)
        {
            // load the initial MicroFragment
            Bundle nestedExtras = getIntent().getBundleExtra("com.schedjoules.nestedExtras");
            MicroFragment initialMicroFragment = nestedExtras.getParcelable("MicroFragment");
            new SimpleMicroFragmentFlow(initialMicroFragment, R.id.schedjoules_microfragment_host).start(this);
        }
    }
}
