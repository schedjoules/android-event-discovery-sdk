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

package com.schedjoules.eventdiscovery.testutils.espresso;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;


/**
 * Various UI test util methods collected here for easy access.
 *
 * @author Gabor Keszthelyi
 */
public final class TestUtils
{
    public static void waitForMillis(long millis)
    {
        Waiter.doWait(millis);
    }


    public static void waitForSeconds(long seconds)
    {
        waitForMillis(seconds * 1000);
    }


    public static void waitForMinutes(long minutes)
    {
        waitForSeconds(minutes * 60);
    }


    public static void toLandscape()
    {
        onView(isRoot()).perform(OrientationChangeAction.landscape());
    }


    public static void toPortrait()
    {
        onView(isRoot()).perform(OrientationChangeAction.portrait());
    }

}
