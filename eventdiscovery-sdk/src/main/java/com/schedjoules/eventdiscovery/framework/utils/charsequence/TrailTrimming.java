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

package com.schedjoules.eventdiscovery.framework.utils.charsequence;

import android.support.annotation.NonNull;


/**
 * {@link CharSequence} decorator that trims trailing white spaces.
 *
 * @author Gabor Keszthelyi
 */
public class TrailTrimming implements CharSequence
{
    private final CharSequence mOriginal;
    private CharSequence mCachedValue;


    public TrailTrimming(@NonNull CharSequence original)
    {
        mOriginal = original;
    }


    @Override
    public int length()
    {
        return value().length();
    }


    @Override
    public char charAt(int index)
    {
        return value().charAt(index);
    }


    @Override
    public CharSequence subSequence(int start, int end)
    {
        return value().subSequence(start, end);
    }


    @NonNull
    @Override
    public String toString()
    {
        return value().toString();
    }


    private CharSequence value()
    {
        if (mCachedValue == null)
        {
            mCachedValue = trimTrailingWhitespaces(mOriginal);
        }
        return mCachedValue;
    }


    // http://stackoverflow.com/a/10187511
    private CharSequence trimTrailingWhitespaces(CharSequence original)
    {
        int i = original.length();
        while (--i >= 0 && Character.isWhitespace(original.charAt(i)))
        {
        }
        return original.subSequence(0, i + 1);
    }
}
