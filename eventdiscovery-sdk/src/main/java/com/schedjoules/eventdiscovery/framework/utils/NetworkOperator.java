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
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;


/**
 * A {@link CharSequence} containing the name of the current network operator. May be {@code "unknown"} on devices without SIM card.
 *
 * @author Marten Gajda
 */
public final class NetworkOperator implements CharSequence
{
    private final Context mContext;
    private String mCachedValue;


    public NetworkOperator(Context context)
    {
        mContext = context.getApplicationContext();
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
        synchronized (this)
        {
            if (mCachedValue == null)
            {
                TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                mCachedValue = tm.getNetworkOperatorName();
                if (mCachedValue == null)
                {
                    mCachedValue = "unknown";
                }
            }
        }
        return mCachedValue;
    }
}
