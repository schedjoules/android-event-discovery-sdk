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

package com.schedjoules.eventdiscovery.framework.model;

import com.schedjoules.client.eventsdiscovery.Event;

import org.dmfs.httpessentials.types.Link;

import java.util.List;


/**
 * Basic implementation for {@link EnrichedEvent}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicEnrichedEvent implements EnrichedEvent
{
    private final Event mEvent;
    private final Iterable<Link> mActionLinks;


    public BasicEnrichedEvent(Event event, Iterable<Link> actionLinks)
    {
        mEvent = event;
        mActionLinks = actionLinks;

    }


    @Override
    public Event event()
    {
        return mEvent;
    }


    @Override
    public Iterable<Link> actions()
    {
        return mActionLinks;
    }
}
