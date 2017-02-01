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

import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isRoot;


/**
 * @author Gabor Keszthelyi
 */
final class Waiter
{

    static void doWait(long millis)
    {
        Espresso.onView(isRoot()).perform(waitFor(millis));
    }


    /**
     * http://stackoverflow.com/a/35924943
     * <p>
     * Perform action of waiting for a specific time.
     */
    private static ViewAction waitFor(final long millis)
    {
        return new ViewAction()
        {
            @Override
            public Matcher<View> getConstraints()
            {
                return isRoot();
            }


            @Override
            public String getDescription()
            {
                return "Waiter for " + millis + " milliseconds.";
            }


            @Override
            public void perform(UiController uiController, View view)
            {
                uiController.loopMainThreadForAtLeast(millis);
            }

        };
    }
}
