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

package com.schedjoules.eventdiscovery.framework.utils.spanned;

import android.text.Spannable;

import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.SimpleLazy;


/**
 * Abstract base class for a {@link Spannable} that uses the provided factory to create and cache the delegate value.
 *
 * @author Gabor Keszthelyi
 */
public abstract class AbstractSpannable implements Spannable
{
    private final Lazy<Spannable> mDelegate;


    public AbstractSpannable(com.schedjoules.eventdiscovery.framework.utils.factory.Factory<Spannable> spannableFactory)
    {
        mDelegate = new SimpleLazy<>(spannableFactory);
    }


    @Override
    public final <T> T[] getSpans(int start, int end, Class<T> type)
    {
        return mDelegate.get().getSpans(start, end, type);
    }


    @Override
    public final int getSpanStart(Object tag)
    {
        return mDelegate.get().getSpanStart(tag);
    }


    @Override
    public final int getSpanEnd(Object tag)
    {
        return mDelegate.get().getSpanEnd(tag);
    }


    @Override
    public final int getSpanFlags(Object tag)
    {
        return mDelegate.get().getSpanFlags(tag);
    }


    @Override
    public final int nextSpanTransition(int start, int limit, Class type)
    {
        return mDelegate.get().nextSpanTransition(start, limit, type);
    }


    @Override
    public final int length()
    {
        return mDelegate.get().length();
    }


    @Override
    public final char charAt(int index)
    {
        return mDelegate.get().charAt(index);
    }


    @Override
    public final CharSequence subSequence(int start, int end)
    {
        return mDelegate.get().subSequence(start, end);
    }


    @Override
    public final String toString()
    {
        return mDelegate.get().toString();
    }


    @Override
    public void setSpan(Object what, int start, int end, int flags)
    {
        mDelegate.get().setSpan(what, start, end, flags);
    }


    @Override
    public void removeSpan(Object what)
    {
        mDelegate.get().removeSpan(what);
    }
}
