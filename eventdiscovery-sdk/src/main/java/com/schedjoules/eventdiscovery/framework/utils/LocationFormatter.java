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
import org.dmfs.iterators.decorators.Filtered;
import org.dmfs.iterators.filters.NonNull;


/**
 * Util for creating formatted texts to display locations.
 *
 * @author Gabor Keszthelyi
 */
public final class LocationFormatter
{

    public static String longLocationFormat(Iterable<Location> locations)
    {
        Location location = locations.iterator().next();
        Address address = location.address();
        return TextUtils.join(", ", new Repeatable<>(
                new Filtered<>(
                        new ArrayIterator<>(location.name(), address.street(), address.locality(), address.country()),
                        NonNull.<String>instance())));
    }

}
