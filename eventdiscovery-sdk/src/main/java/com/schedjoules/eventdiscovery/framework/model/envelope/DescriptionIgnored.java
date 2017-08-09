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

package com.schedjoules.eventdiscovery.framework.model.envelope;

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;

import org.dmfs.iterators.Function;
import org.dmfs.optional.Optional;
import org.dmfs.optional.decorators.Mapped;


/**
 * A {@link Envelope<Event>} decorator that ignores {@link Event#description()}, i.e. uses "" empty string as their value.
 *
 * @author Gabor Keszthelyi
 */
public final class DescriptionIgnored implements Envelope<Event>
{
    private final Envelope<Event> mOriginal;


    public DescriptionIgnored(Envelope<Event> original)
    {
        mOriginal = original;
    }


    @Override
    public String etag()
    {
        return mOriginal.etag();
    }


    @Override
    public String uid()
    {
        return mOriginal.uid();
    }


    @Override
    public Optional<Event> payload()
    {
        return new Mapped<>(new Function<Event, Event>()
        {
            @Override
            public Event apply(Event input)
            {
                return new com.schedjoules.eventdiscovery.framework.model.event.DescriptionIgnored(input);
            }
        }, mOriginal.payload());
    }
}
