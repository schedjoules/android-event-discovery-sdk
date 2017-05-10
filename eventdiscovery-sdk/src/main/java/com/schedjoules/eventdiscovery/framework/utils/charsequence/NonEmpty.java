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

import android.support.annotation.Nullable;

import org.dmfs.optional.Optional;

import java.util.NoSuchElementException;


/**
 * {@link Optional<CharSequence>} which is present when the provided delegate value is not null and not empty.
 *
 * @author Gabor Keszthelyi
 */
public final class NonEmpty implements Optional<CharSequence>
{
    private final CharSequence mDelegate;


    public NonEmpty(@Nullable CharSequence delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public boolean isPresent()
    {
        return mDelegate != null && mDelegate.length() != 0;
    }


    @Override
    public CharSequence value(CharSequence defaultValue)
    {
        return isPresent() ? mDelegate : defaultValue;
    }


    @Override
    public CharSequence value() throws NoSuchElementException
    {
        if (!isPresent())
        {
            throw new NoSuchElementException("Delegate CharSequence is null or empty");
        }
        return mDelegate;
    }
}
