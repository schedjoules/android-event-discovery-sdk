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

package com.schedjoules.eventdiscovery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.eventdetails.EventDetailActivity;
import com.schedjoules.eventdiscovery.eventdetails.wizardsteps.EventLoaderStep;


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
        Intent intent = new Intent(activity, EventDetailActivity.class);
        Bundle nestedBundle = new Bundle();
        nestedBundle.putParcelable("WizardStep", new EventLoaderStep(mEventUid));
        intent.putExtra("com.schedjoules.nestedExtras", nestedBundle);
        activity.startActivity(intent);
    }
}
