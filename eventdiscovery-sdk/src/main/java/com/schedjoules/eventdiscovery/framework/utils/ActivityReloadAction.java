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

package com.schedjoules.eventdiscovery.framework.utils;

import android.app.Activity;

import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;


/**
 * {@link OnClickAction} that reloads the given Activity.
 *
 * @author Gabor Keszthelyi
 */
public final class ActivityReloadAction implements OnClickAction
{
    private final Activity mActivity;


    public ActivityReloadAction(Activity activity)
    {
        mActivity = activity;
    }


    @Override
    public void onClick()
    {
        mActivity.finish();
        mActivity.startActivity(mActivity.getIntent());
    }
}
