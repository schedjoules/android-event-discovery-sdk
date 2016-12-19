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

package com.schedjoules.eventdiscovery.proxies;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.schedjoules.eventdiscovery.UidEventDetails;


/**
 * Invisible {@link Activity} to handle deep links to the event details web site.
 *
 * @author Marten Gajda
 */
public final class DeeplinkEventDetailsProxy extends Activity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        new UidEventDetails(getIntent().getData().getLastPathSegment()).show(this);
        finish();
    }
}
