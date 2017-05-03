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

package com.schedjoules.eventdiscovery.framework.model.recent.places;

import com.schedjoules.client.eventsdiscovery.locations.StructuredGeoLocation;
import com.schedjoules.eventdiscovery.framework.model.location.geoplace.GeoPlace;
import com.schedjoules.eventdiscovery.framework.model.location.geoplace.StructuredGeoPlace;
import com.schedjoules.eventdiscovery.framework.model.location.namedplace.StructuredNamedPlace;
import com.schedjoules.eventdiscovery.framework.model.recent.CharSequenceConverter;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * @author Marten Gajda
 */
public final class GeoPlaceCharSequenceConverterTest
{
    @Test
    public void fromValue() throws Exception
    {
        CharSequenceConverter<GeoPlace> factory = new GeoPlaceCharSequenceConverter();

        assertEquals("geo:1.000000,2.000000?id=someId&name=someName&extra=someExtra",
                factory.fromValue(new StructuredGeoPlace(new StructuredNamedPlace("someId", "someName", "someExtra"), new StructuredGeoLocation(1, 2)))
                        .toString());
    }


    @Test
    public void fromCharSequence() throws Exception
    {
        CharSequenceConverter<GeoPlace> factory = new GeoPlaceCharSequenceConverter();
        assertEquals(1.0f, factory.fromCharSequence("geo:1,2?id=someId&name=someName&extra=someExtra").geoLocation().latitude());
        assertEquals(1.0f, factory.fromCharSequence("geo:1.000000,2.000000?id=someId&name=someName&extra=someExtra").geoLocation().latitude());
        assertEquals(2.0f, factory.fromCharSequence("geo:1,2?id=someId&name=someName&extra=someExtra").geoLocation().longitude());
        assertEquals(2.0f, factory.fromCharSequence("geo:1.000000,2.000000?id=someId&name=someName&extra=someExtra").geoLocation().longitude());
        assertEquals("someId", factory.fromCharSequence("geo:1.000000,2.000000?id=someId&name=someName&extra=someExtra").namedPlace().id());
        assertEquals("someName", factory.fromCharSequence("geo:1.000000,2.000000?id=someId&name=someName&extra=someExtra").namedPlace().name());
        assertEquals("someExtra", factory.fromCharSequence("geo:1.000000,2.000000?id=someId&name=someName&extra=someExtra").namedPlace().extraContext());
    }
}