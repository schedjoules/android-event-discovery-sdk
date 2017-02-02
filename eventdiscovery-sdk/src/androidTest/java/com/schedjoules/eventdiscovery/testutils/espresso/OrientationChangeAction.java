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

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import org.hamcrest.Matcher;


/**
 * An Espresso {@link ViewAction} that changes the mOrientation of the screen
 * <p>
 * https://gist.githubusercontent.com/nbarraille/03e8910dc1d415ed9740/raw/32a15f2495a604e6ab15b25b2277321b95f69e2d/OrientationChangeAction.java
 */
class OrientationChangeAction implements ViewAction
{
    private final int mOrientation;


    private OrientationChangeAction(int orientation)
    {
        mOrientation = orientation;
    }


    static ViewAction landscape()
    {
        return new OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    static ViewAction portrait()
    {
        return new OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public Matcher<View> getConstraints()
    {
        return ViewMatchers.isRoot();
    }


    @Override
    public String getDescription()
    {
        return "Change orientation to " + mOrientation;
    }


    @Override
    public void perform(UiController uiController, View view)
    {
        uiController.loopMainThreadUntilIdle();
        Activity activity = (Activity) view.getContext();
        activity.setRequestedOrientation(mOrientation);
    }
}