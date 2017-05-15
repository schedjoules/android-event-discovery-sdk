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

import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.parameters.BasicParameterType;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.types.Link;


/**
 * Basic implementation of {@link TicketInfo} that checks for sale-status for availability.
 * <p>
 * Note: Currently only 2 states of the ticket button is used, so this maps all statuses except 'unavailable' to true.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicTicketInfo implements TicketInfo
{
    private final static ParameterType<String> SALE_STATUS =
            new BasicParameterType<>("http://schedjoules.com/props/booking/sale-status", new PlainStringHeaderConverter());

    private final Link mLink;


    public BasicTicketInfo(Link ticketLink)
    {
        mLink = ticketLink;
    }


    @Override
    public boolean isAvailable()
    {
        return !mLink.firstParameter(SALE_STATUS, "default").value().equals("unavailable");
    }
}
