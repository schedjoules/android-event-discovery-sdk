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

package com.schedjoules.eventdiscovery.testutils;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.Location;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;

import java.util.Collections;


/**
 * @author Gabor Keszthelyi
 */
public final class DummyEvent implements Event
{

    private final String mTitle;
    private final DateTime mStart;


    public DummyEvent(String title, DateTime start)
    {
        mTitle = title;
        mStart = start;
    }


    public DummyEvent(DateTime start)
    {
        this("dummy title", start);
    }


    @Override
    public String uid()
    {
        return "dummy uid";
    }


    @Override
    public DateTime start()
    {
        return mStart;
    }


    @Override
    public Optional<Duration> duration()
    {
        return new Present<>(new Duration(1, 0, 2, 0, 0));
    }


    @Override
    public String title()
    {
        return mTitle;
    }


    @Override
    public String description()
    {
        return "dummy desc";
    }


    @Override
    public Iterable<Location> locations()
    {
        return Collections.emptyList();
    }


    @Override
    public Iterable<Link> links()
    {
        return Collections.emptyList();
    }
}
