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
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.schedjoules.client.eventsdiscovery.GeoLocation;

import org.dmfs.rfc5545.DateTime;


/**
 * Interface of a builder for an event discovery.
 *
 * @author Marten Gajda
 */
public interface EventDiscovery
{
    /**
     * Returns a new {@link EventDiscovery} that starts at the given {@link DateTime}.
     *
     * @param dateTime
     *         The {@link DateTime} of the first event to show.
     *
     * @return A new {@link EventDiscovery} object.
     */
    @NonNull
    EventDiscovery withStart(@NonNull DateTime dateTime);

    /**
     * Returns a new {@link EventDiscovery} at the given {@link GeoLocation}.
     *
     * @param location
     *         The {@link GeoLocation}.
     *
     * @return A new {@link EventDiscovery} object.
     */
    @NonNull
    EventDiscovery withLocation(@NonNull GeoLocation location);

    /**
     * Returns a new {@link EventDiscovery} at the given {@link GeoLocation} in the given radius.
     *
     * @param location
     *         The {@link GeoLocation}.
     * @param radius
     *         The radius.
     *
     * @return A new {@link EventDiscovery} object.
     */
    @NonNull
    EventDiscovery withLocation(@NonNull GeoLocation location, int radius);

    /**
     * Starts the event discovery with a specific theme. This should be either {@code SchedJoules_Theme.Default}, {@code SchedJoules_Theme.Light},
     * {@code SchedJoules_Theme.Dark} or any theme that inherits from these.
     *
     * @param theme
     *         The resource id of the theme to use.
     *
     * @return A new {@link EventDiscovery} using the given theme.
     */
    EventDiscovery withTheme(@StyleRes int theme);

    /**
     * Starts this {@link EventDiscovery}.
     *
     * @param activity
     *         An {@link Activity}.
     */
    void start(@NonNull Activity activity);
}
