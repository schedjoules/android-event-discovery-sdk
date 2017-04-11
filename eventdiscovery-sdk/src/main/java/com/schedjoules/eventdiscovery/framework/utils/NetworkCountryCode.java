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
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.optional.Optional;

import java.util.NoSuchElementException;


/**
 * An {@link Optional} {@link Token} that represents the country code of the current operator. This is merely a best guess and not always accurate, though for
 * most mobile devices it should work.
 *
 * @author Marten Gajda
 */
public final class NetworkCountryCode implements Optional<Token>
{
    private final Context mContext;
    private Token mCachedValue;


    /**
     * Creates a new {@link NetworkCountryCode}.
     *
     * @param context
     *         A {@link Context}.
     */
    public NetworkCountryCode(Context context)
    {
        mContext = context.getApplicationContext();
    }


    @Override
    public boolean isPresent()
    {
        return countryCodeToken() != null;
    }


    @Override
    public Token value(Token defaultValue)
    {
        Token token = countryCodeToken();
        return token == null ? defaultValue : token;
    }


    @Override
    public Token value() throws NoSuchElementException
    {
        Token token = countryCodeToken();
        if (token == null)
        {
            throw new NoSuchElementException("Could not determine network operator country");
        }
        return token;
    }


    private Token countryCodeToken()
    {
        if (mCachedValue == null)
        {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String country = tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA ? null : tm.getNetworkCountryIso();
            mCachedValue = TextUtils.isEmpty(country) ? null : new StringToken(country);
        }
        return mCachedValue;
    }
}
