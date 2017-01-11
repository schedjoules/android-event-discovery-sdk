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
import android.content.pm.PackageManager;

import com.schedjoules.client.insights.Client;

import java.util.Locale;


/**
 * Created by marten on 02.12.16.
 */
public final class AndroidClient implements Client
{
    private final Context mContext;


    public AndroidClient(Context context)
    {
        mContext = context;
    }


    @Override
    public CharSequence id()
    {
        return mContext.getPackageName();
    }


    @Override
    public CharSequence version()
    {
        try
        {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Own package name not found.");
        }
    }


    @Override
    public CharSequence networkOperator()
    {
        return new NetworkOperator(mContext);
    }


    @Override
    public Locale locale()
    {
        return Locale.getDefault();
    }

}
