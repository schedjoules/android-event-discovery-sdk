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

import android.text.TextUtils;

import com.schedjoules.client.eventsdiscovery.Location;

import org.dmfs.optional.Optional;

import java.util.NoSuchElementException;


/**
 * The location name (venue name).
 *
 * @author Gabor Keszthelyi
 */
public final class VenueName implements Optional<CharSequence>
{
    private final Iterable<Location> mLocations;


    public VenueName(Iterable<Location> locations)
    {
        mLocations = locations;
    }


    @Override
    public boolean isPresent()
    {
        return mLocations.iterator().hasNext() && !TextUtils.isEmpty(mLocations.iterator().next().name());
    }


    @Override
    public CharSequence value(CharSequence defaultValue)
    {
        return isPresent() ? value() : defaultValue;
    }


    @Override
    public CharSequence value() throws NoSuchElementException
    {
        if (!isPresent())
        {
            throw new NoSuchElementException("No location name");
        }
        return mLocations.iterator().next().name();
    }
}
