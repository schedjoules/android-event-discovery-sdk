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
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.schedjoules.client.UserIdentifier;

import java.util.UUID;


/**
 * A {@link UserIdentifier} that's stored in/retrieved from the {@link SharedPreferences}.
 *
 * @author Marten Gajda
 */
public final class SharedPrefsUserIdentifier implements UserIdentifier
{
    private final static String KEY = "user_identifier";
    private final Context mContext;

    private String mCachedId;


    public SharedPrefsUserIdentifier(Context context)
    {
        mContext = context;
    }


    @Override
    public int length()
    {
        return toString().length();
    }


    @Override
    public char charAt(int index)
    {
        return toString().charAt(index);
    }


    @Override
    public CharSequence subSequence(int start, int end)
    {
        return toString().subSequence(start, end);
    }


    @NonNull
    @Override
    public String toString()
    {
        synchronized (SharedPrefsUserIdentifier.class)
        {
            if (mCachedId == null)
            {
                SharedPreferences preferences = prefs();
                mCachedId = preferences.getString(KEY, null);
                if (mCachedId == null)
                {
                    mCachedId = UUID.randomUUID().toString();
                    preferences.edit().putString(KEY, mCachedId).apply();
                }
            }
        }
        return mCachedId;
    }


    private SharedPreferences prefs()
    {
        return mContext.getSharedPreferences("schedjoules_prefs", 0);
    }
}
