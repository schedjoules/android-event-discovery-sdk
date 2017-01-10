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

package com.schedjoules.eventdiscovery.discovery;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.framework.model.ParcelableGeoLocation;

import org.dmfs.rfc5545.DateTime;

import static com.schedjoules.eventdiscovery.framework.EventIntents.EXTRA_GEOLOCATION;
import static com.schedjoules.eventdiscovery.framework.EventIntents.EXTRA_RADIUS;
import static com.schedjoules.eventdiscovery.framework.EventIntents.EXTRA_START_AFTER_TIMESTAMP;


/**
 * A basic {@link EventDiscovery} implementation.
 *
 * @author Marten Gajda
 */
public final class BasicEventDiscovery implements EventDiscovery
{
    @NonNull
    private final Intent mIntent;


    public BasicEventDiscovery()
    {
        this(new Intent("schedjoules.intent.action.EVENT_DISCOVERY"));
    }


    private BasicEventDiscovery(@NonNull Intent intent)
    {
        mIntent = intent;
    }


    @NonNull
    @Override
    public EventDiscovery withStart(@NonNull DateTime dateTime)
    {
        return new BasicEventDiscovery(new Intent(mIntent).putExtra(EXTRA_START_AFTER_TIMESTAMP, dateTime.getTimestamp()));
    }


    @NonNull
    @Override
    public EventDiscovery withLocation(@NonNull GeoLocation location)
    {
        return new BasicEventDiscovery(new Intent(mIntent).putExtra(EXTRA_GEOLOCATION, new ParcelableGeoLocation(location)));
    }


    @NonNull
    @Override
    public EventDiscovery withLocation(@NonNull GeoLocation location, int radius)
    {
        return new BasicEventDiscovery(new Intent(mIntent).putExtra(EXTRA_GEOLOCATION, new ParcelableGeoLocation(location)).putExtra(EXTRA_RADIUS, radius));
    }


    @Override
    public void start(@NonNull Activity activity)
    {
        activity.startActivity(new Intent(mIntent).setPackage(activity.getPackageName()));
    }
}
