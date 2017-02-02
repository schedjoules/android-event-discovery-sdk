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

package com.schedjoules.eventdiscovery.testutils;

import org.dmfs.rfc5545.Duration;


/**
 * Convenience util methods related to {@link Duration}.
 *
 * @author Gabor Keszthelyi
 */
public final class Durations
{

    public static Duration days(int days)
    {
        return new Duration(1, days, 0);
    }


    public static Duration hours(int hours)
    {
        return new Duration(1, 0, hours, 0, 0);
    }


    public static Duration reverse(Duration duration)
    {
        return new Duration(-1, duration.getDays(), duration.getHours(), duration.getMinutes(),
                duration.getSeconds());
    }
}
