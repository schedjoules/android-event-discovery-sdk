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

package com.schedjoules.eventdiscovery.framework.common;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.schedjoules.eventdiscovery.framework.utils.Color;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * Represents the status bar with updatable color.
 *
 * @author Gabor Keszthelyi
 */
public final class StatusBar implements SmartView<Color>
{
    private final Activity mActivity;


    public StatusBar(Activity activity)
    {
        mActivity = activity;
    }


    @Override
    public void update(Color color)
    {
        // http://stackoverflow.com/a/26749343/4247460
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color.argb());
        }
    }
}
