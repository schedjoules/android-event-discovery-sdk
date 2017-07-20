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

package com.schedjoules.eventdiscovery.framework.model.event;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.Location;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.Optional;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;


/**
 * A {@link Event} decorator that ignores the {@link Event#description()} property, i.e. uses "" empty string as their value.
 *
 * @author Gabor Keszthelyi
 */
public final class DescriptionIgnored implements Event
{
    private final Event mOriginal;


    public DescriptionIgnored(Event original)
    {
        mOriginal = original;
    }


    @Override
    public String uid()
    {
        return mOriginal.uid();
    }


    @Override
    public DateTime start()
    {
        return mOriginal.start();
    }


    @Override
    public Optional<Duration> duration()
    {
        return mOriginal.duration();
    }


    @Override
    public String title()
    {
        return mOriginal.title();
    }


    @Override
    public String description()
    {
        return "";
    }


    @Override
    public Iterable<Location> locations()
    {
        return mOriginal.locations();
    }


    @Override
    public Iterable<Link> links()
    {
        return mOriginal.links();
    }
}
