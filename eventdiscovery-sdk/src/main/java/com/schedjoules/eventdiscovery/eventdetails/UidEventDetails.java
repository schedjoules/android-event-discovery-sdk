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

package com.schedjoules.eventdiscovery.eventdetails;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.activities.MicroFragmentHostActivity;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.EventLoaderMicroFragment;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.IntentBuilder;

import org.dmfs.android.microfragments.MicroFragment;


/**
 * {@link EventDetails} of an {@link Event} identified by its UID.
 *
 * @author Marten Gajda
 */
public final class UidEventDetails implements EventDetails
{
    private final String mEventUid;


    public UidEventDetails(@NonNull String eventUid)
    {
        mEventUid = eventUid;
    }


    @Override
    public void show(@NonNull Activity activity)
    {
        activity.startActivity(
                new IntentBuilder(activity, MicroFragmentHostActivity.class)
                        .with(Keys.MICRO_FRAGMENT, new ParcelableBox<MicroFragment>(new EventLoaderMicroFragment(mEventUid)))
                        .build());
    }

}
