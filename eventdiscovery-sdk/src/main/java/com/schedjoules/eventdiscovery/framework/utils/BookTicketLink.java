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

import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.parameters.BasicParameterType;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.Optional;

import java.util.NoSuchElementException;


/**
 * The {@link Link} for the book reltype with ticket subtype.
 *
 * @author Gabor Keszthelyi
 */
public class BookTicketLink implements Optional<Link>
{
    private final static ParameterType<String> TYPE = new BasicParameterType<>("http://schedjoules.com/booking/type", new PlainStringHeaderConverter());

    private final Iterable<Link> mLinks;

    private Link mCachedValue;


    public BookTicketLink(Iterable<Link> links)
    {
        mLinks = links;
    }


    @Override
    public boolean isPresent()
    {
        return bookTicketLink() != null;
    }


    @Override
    public Link value(Link defaultValue)
    {
        return bookTicketLink() != null ? bookTicketLink() : defaultValue;
    }


    @Override
    public Link value() throws NoSuchElementException
    {
        if (bookTicketLink() == null)
        {
            throw new NoSuchElementException("No 'book' link with type 'ticket'");
        }
        return bookTicketLink();
    }


    private Link bookTicketLink()
    {
        if (mCachedValue == null)
        {
            for (Link link : mLinks)
            {
                if (link.relationTypes().contains("http://schedjoules.com/rel/action/book"))
                {
                    String ticket = link.firstParameter(TYPE, "ticket").value();
                    if (ticket.equals("ticket"))
                    {
                        mCachedValue = link;
                        break;
                    }
                }
            }
        }
        return mCachedValue;
    }

}
