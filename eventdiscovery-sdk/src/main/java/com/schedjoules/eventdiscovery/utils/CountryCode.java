package com.schedjoules.eventdiscovery.utils;

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
