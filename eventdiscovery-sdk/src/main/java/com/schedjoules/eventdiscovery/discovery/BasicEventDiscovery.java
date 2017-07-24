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
import android.support.annotation.StyleRes;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.framework.eventlist.EventListMicroFragment;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.DateTimeBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.GeoLocationBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.IntBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.IntentBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.commons.NestedBundle;
import com.schedjoules.eventdiscovery.framework.serialization.core.FluentBuilder;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.rfc5545.DateTime;


/**
 * A basic {@link EventDiscovery} implementation.
 *
 * @author Marten Gajda
 */
public final class BasicEventDiscovery implements EventDiscovery
{
    private final FluentBuilder<Intent> mIntentBuilder;


    public BasicEventDiscovery()
    {
        this(new IntentBuilder("schedjoules.intent.action.EVENT_DISCOVERY"));
    }


    private BasicEventDiscovery(FluentBuilder<Intent> intentBuilder)
    {
        mIntentBuilder = intentBuilder;
    }


    @NonNull
    @Override
    public EventDiscovery withStart(@NonNull DateTime dateTime)
    {
        return new BasicEventDiscovery(mIntentBuilder.with(Keys.DATE_TIME_START_AFTER, new DateTimeBox(dateTime)));
    }


    @NonNull
    @Override
    public EventDiscovery withLocation(@NonNull GeoLocation location)
    {
        return new BasicEventDiscovery(mIntentBuilder.with(Keys.GEO_LOCATION, new GeoLocationBox(location)));
    }


    @NonNull
    @Override
    public EventDiscovery withLocation(@NonNull GeoLocation location, int radius)
    {
        return new BasicEventDiscovery(mIntentBuilder
                .with(Keys.GEO_LOCATION, new GeoLocationBox(location))
                .with(Keys.LOCATION_RADIUS, new IntBox(radius)));
    }


    @Override
    public EventDiscovery withTheme(@StyleRes int theme)
    {
        return new BasicEventDiscovery(mIntentBuilder.with(Keys.THEME, new IntBox(theme)));
    }


    @Override
    public void start(@NonNull Activity activity)
    {
        activity.startActivity(mIntentBuilder
                .with(Keys.MICRO_FRAGMENT, new ParcelableBox<MicroFragment>(
                        new EventListMicroFragment(new NestedBundle(mIntentBuilder.build()).get())))
                .build().setPackage(activity.getPackageName()));
    }
}
