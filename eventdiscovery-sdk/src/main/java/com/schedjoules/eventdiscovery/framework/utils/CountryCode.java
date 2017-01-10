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

import org.dmfs.httpessentials.types.Token;

import java.util.Locale;


/**
 * A token that represents the country code of the current country. This is merely a best guess and not always accurate, though for most mobile devices it
 * should work. Falls back to the locale region setting.
 *
 * @author Marten Gajda
 */
public final class CountryCode implements Token
{
    private final Context mContext;
    private String mCachedValue;


    /**
     * Creates a new {@link CountryCode}.
     *
     * @param context
     *         A {@link Context}.
     */
    public CountryCode(Context context)
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
                if (tm.getNetworkType() != TelephonyManager.NETWORK_TYPE_CDMA)
                {
                    mCachedValue = tm.getNetworkCountryIso();
                }

                if (mCachedValue == null)
                {
                    mCachedValue = Locale.getDefault().getCountry();
                }
            }
        }
        return mCachedValue;
    }
}
