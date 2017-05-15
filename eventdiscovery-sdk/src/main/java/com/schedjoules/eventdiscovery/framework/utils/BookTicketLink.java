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

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.optionals.AbstractCachingOptional;
import com.schedjoules.eventdiscovery.framework.utils.optionals.First;

import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.parameters.BasicParameterType;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.iterators.AbstractFilteredIterator;
import org.dmfs.iterators.FilteredIterator;
import org.dmfs.optional.Optional;


/**
 * The {@link Link} for the book reltype with ticket subtype.
 *
 * @author Gabor Keszthelyi
 */
public class BookTicketLink extends AbstractCachingOptional<Link>
{
    private static final String BOOK_REL_TYPE = "http://schedjoules.com/rel/action/book";
    private static final String TICKET_SUB_TYPE = "ticket";
    private static final ParameterType<String> SUB_TYPE_PARAMETER =
            new BasicParameterType<>("http://schedjoules.com/props/booking/type", new PlainStringHeaderConverter());


    public BookTicketLink(final Iterable<Link> links)
    {
        super(new Factory<Optional<Link>>()
        {
            @Override
            public Optional<Link> create()
            {
                return new First<>(
                        new FilteredIterator<>(links.iterator(),
                                new AbstractFilteredIterator.IteratorFilter<Link>()
                                {
                                    @Override
                                    public boolean iterate(Link link)
                                    {
                                        return link.relationTypes().contains(BOOK_REL_TYPE)
                                                && link.firstParameter(SUB_TYPE_PARAMETER, "not-used").value().equals(TICKET_SUB_TYPE);
                                    }
                                }));
            }
        });
    }

}
