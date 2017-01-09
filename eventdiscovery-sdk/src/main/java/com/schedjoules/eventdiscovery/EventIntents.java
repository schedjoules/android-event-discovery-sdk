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

/**
 * Intent constants and methods related to public features of the SDK.
 *
 * @author Gabor Keszthelyi
 */
public final class EventIntents
{

    /**
     * UTC timestamp for starting date-time for fetching events.
     */
    public static final String EXTRA_START_AFTER_TIMESTAMP = "schedjoules.event.intent.extra.START_AFTER";

    /**
     */
    public static final String EXTRA_GEOLOCATION = "schedjoules.event.intent.extra.GEOLOCATION";

    /**
     */
    public static final String EXTRA_RADIUS = "schedjoules.event.intent.extra.RADIUS";

    /**
     * Intent action for adding an event to the calendar. The default operation for the "Add to calendar" button will look for Activities registered for this
     * Action first, so clients can make sure to direct user to their calendar without showing chooser to user.
     */
    public static final String ACTION_ADD_TO_CALENDAR = "schedjoules.event.intent.action.ADD_TO_CALENDAR";

    /**
     * Extra for the event uid field sent in the add to calendar intent.
     */
    public static final String EXTRA_SCHEDJOULES_EVENT_UID = "schedjoules.event.intent.extra.UID";

    public static final String EXTRA_GEO_PLACE = "schedjoules.event.intent.extra.GEO_PLACE";
}
