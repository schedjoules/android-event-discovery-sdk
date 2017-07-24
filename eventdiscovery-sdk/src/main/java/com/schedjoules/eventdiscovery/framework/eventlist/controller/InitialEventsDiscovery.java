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

package com.schedjoules.eventdiscovery.framework.eventlist.controller;

import com.schedjoules.client.Api;
import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.State;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.EventsDiscovery;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.client.eventsdiscovery.queries.SimpleEventsDiscovery;
import com.schedjoules.eventdiscovery.framework.model.location.geolocation.UndefinedGeoLocation;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.rfc5545.DateTime;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.TimeZone;


/**
 * The query that is used to fetch the first page when the Event List screen loads.
 *
 * @author Gabor Keszthelyi
 */
public final class InitialEventsDiscovery implements ApiQuery<ResultPage<Envelope<Event>>>
{
    private final DateTime mDateTime;
    private final GeoLocation mLocation;


    public InitialEventsDiscovery(DateTime dateTime, GeoLocation location)
    {
        mDateTime = dateTime;
        mLocation = location;
    }


    @Override
    public ResultPage<Envelope<Event>> queryResult(Api api) throws IOException, URISyntaxException, ProtocolError, ProtocolException
    {
        // convert any floating time to absolute time using the current time zone
        DateTime startAfter = mDateTime.isFloating() ? mDateTime.swapTimeZone(TimeZone.getDefault()) : mDateTime;

        EventsDiscovery query = new SimpleEventsDiscovery()
                .withStartAtOrAfter(startAfter);

        if (!(mLocation instanceof UndefinedGeoLocation))
        {
            query = query.withGeoLocation(mLocation, 10000 /*meters*/);
        }

        query = query.withResultsLimit(50);

        return query.queryResult(api);
    }


    @Override
    public State<ApiQuery<ResultPage<Envelope<Event>>>> serializable()
    {
        throw new UnsupportedOperationException("InitialEventsDiscovery doesn't support a serializable state yet.");
    }
}
