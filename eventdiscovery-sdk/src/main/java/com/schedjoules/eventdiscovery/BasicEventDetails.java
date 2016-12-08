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
import com.schedjoules.eventdiscovery.model.ParcelableEvent;

import static com.schedjoules.eventdiscovery.eventdetails.EventDetailActivity.CUSTOM_EXTRA_EVENT;
import static com.schedjoules.eventdiscovery.eventdetails.EventDetailActivity.EXTRA_CUSTOM_PARCELABLES;


/**
 * {@link EventDetails} of an {@link Event}.
 *
 * @author Marten Gajda
 */
public final class BasicEventDetails implements EventDetails
{
    private final Event mEvent;


    public BasicEventDetails(@NonNull Event event)
    {
        mEvent = event;
    }


    @Override
    public void show(@NonNull Activity activity)
    {
        Intent intent = new Intent(activity, EventDetailActivity.class);
        Bundle nestedBundle = new Bundle();
        nestedBundle.putParcelable(CUSTOM_EXTRA_EVENT, new ParcelableEvent(mEvent));
        intent.putExtra(EXTRA_CUSTOM_PARCELABLES, nestedBundle);
        activity.startActivity(intent);
    }
}
