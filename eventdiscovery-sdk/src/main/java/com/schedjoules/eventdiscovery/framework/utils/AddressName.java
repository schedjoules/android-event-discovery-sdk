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

import com.schedjoules.client.eventsdiscovery.Address;
import com.schedjoules.client.eventsdiscovery.Location;

import org.dmfs.iterables.Repeatable;
import org.dmfs.iterators.ArrayIterator;
import org.dmfs.iterators.FilteredIterator;
import org.dmfs.iterators.filters.NoneOf;
import org.dmfs.optional.Optional;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Formatted address name as <code>[street][locality][country]</code>
 *
 * @author Gabor Keszthelyi
 */
public final class AddressName implements Optional<CharSequence>
{
    private final Iterable<Location> mLocations;


    public AddressName(Iterable<Location> locations)
    {
        mLocations = locations;
    }


    @Override
    public boolean isPresent()
    {
        try
        {
            value();
            return true;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }


    @Override
    public CharSequence value(CharSequence defaultValue)
    {
        try
        {
            return value();
        }
        catch (NoSuchElementException e)
        {
            return defaultValue;
        }
    }


    @Override
    public CharSequence value() throws NoSuchElementException
    {
        Iterator<Location> iterator = mLocations.iterator();

        if (!iterator.hasNext())
        {
            throw new NoSuchElementException("No locations");
        }

        Address address = iterator.next().address();
        if (address == null)
        {
            throw new NoSuchElementException("No address");
        }

        String value = TextUtils.join(", ", new Repeatable<>(
                new FilteredIterator<>(
                        new ArrayIterator<>(address.street(), address.locality(), address.country()),
                        new NoneOf<>(null, ""))));

        if (TextUtils.isEmpty(value))
        {
            throw new NoSuchElementException("No address fields");
        }

        return value;
    }
}
