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

import android.content.Context;
import android.os.Build;

import com.schedjoules.client.insights.Display;
import com.schedjoules.client.insights.Platform;


/**
 * Created by marten on 02.12.16.
 */
public final class AndroidPlatform implements Platform
{
    private final Context mContext;


    public AndroidPlatform(Context context)
    {
        mContext = context.getApplicationContext();
    }


    @Override
    public CharSequence name()
    {
        return "Android";
    }


    @Override
    public CharSequence vendor()
    {
        return Build.MANUFACTURER;
    }


    @Override
    public CharSequence device()
    {
        return Build.MODEL;
    }


    @Override
    public CharSequence version()
    {
        return String.valueOf(Build.VERSION.SDK_INT);
    }


    @Override
    public Display display()
    {
        return new AndroidDisplay(mContext);
    }
}
