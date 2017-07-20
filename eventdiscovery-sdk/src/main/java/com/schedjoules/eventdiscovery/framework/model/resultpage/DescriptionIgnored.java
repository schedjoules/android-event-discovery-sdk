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

package com.schedjoules.eventdiscovery.framework.model.resultpage;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;

import org.dmfs.iterators.Function;
import org.dmfs.iterators.decorators.Mapped;
import org.dmfs.optional.Optional;

import java.util.Iterator;


/**
 * A {@link ResultPage<Envelope<Event>>} decorator that ignores the all {@link Event#description()}, i.e. uses "" empty string as their value.
 *
 * @author Gabor Keszthelyi
 */
// TODO If this approach is kept, it may be worth handling it in java-api-client side and/or having separate model for event without and with description
public final class DescriptionIgnored implements ResultPage<Envelope<Event>>
{
    private final ResultPage<Envelope<Event>> mOriginal;


    public DescriptionIgnored(ResultPage<Envelope<Event>> original)
    {
        mOriginal = original;
    }


    @Override
    public Optional<ApiQuery<ResultPage<Envelope<Event>>>> previousPageQuery()
    {
        return mOriginal.previousPageQuery();
    }


    @Override
    public Optional<ApiQuery<ResultPage<Envelope<Event>>>> nextPageQuery()
    {
        return mOriginal.nextPageQuery();
    }


    @Override
    public Iterator<Envelope<Event>> iterator()
    {
        return new Mapped<>(mOriginal.iterator(), new Function<Envelope<Event>, Envelope<Event>>()
        {
            @Override
            public Envelope<Event> apply(Envelope<Event> envelope)
            {
                return new com.schedjoules.eventdiscovery.framework.model.envelope.DescriptionIgnored(envelope);
            }
        });
    }
}
