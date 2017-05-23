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

import android.app.Activity;

import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;

import org.dmfs.android.microfragments.MicroFragmentHost;


/**
 * Lazy access to the {@link MicroFragmentHost} used by the {@link Activity}.
 *
 * @author Gabor Keszthelyi
 */
public final class ActivityMicroFragmentHost implements Lazy<MicroFragmentHost>
{
    private final Lazy<MicroFragmentHost> mDelegate;


    public ActivityMicroFragmentHost(Activity activity)
    {
        mDelegate = new Argument<MicroFragmentHost>(Keys.MICRO_FRAGMENT_HOST, activity);
    }


    @Override
    public MicroFragmentHost get()
    {
        return mDelegate.get();
    }
}
