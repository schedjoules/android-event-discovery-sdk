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

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.locations.UriGeoLocation;
import com.schedjoules.eventdiscovery.framework.model.location.geoplace.GeoPlace;
import com.schedjoules.eventdiscovery.framework.model.location.namedplace.NamedPlace;

import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.adapters.TextParameter;
import org.dmfs.rfc3986.parameters.adapters.XwfueParameterList;
import org.dmfs.rfc3986.paths.Text;

import java.net.URI;

import static com.schedjoules.eventdiscovery.framework.model.recent.places.GeoPlaceCharSequenceConverter.PARAM_PLACE_EXTRA;
import static com.schedjoules.eventdiscovery.framework.model.recent.places.GeoPlaceCharSequenceConverter.PARAM_PLACE_ID;
import static com.schedjoules.eventdiscovery.framework.model.recent.places.GeoPlaceCharSequenceConverter.PARAM_PLACE_NAME;


/**
 * A {@link GeoPlace} derived from a {@link Uri}.
 *
 * @author Marten Gajda
 */
public final class UriGeoPlace implements GeoPlace
{
    private final Uri mUri;


    public UriGeoPlace(Uri uri)
    {
        mUri = uri;
    }


    @Override
    public NamedPlace namedPlace()
    {
        final ParameterList parameters = new XwfueParameterList(mUri.query().value());
        return new NamedPlace()
        {
            @Override
            public String id()
            {
                return new TextParameter(PARAM_PLACE_ID, parameters).toString();
            }


            @Override
            public CharSequence name()
            {
                return new TextParameter(PARAM_PLACE_NAME, parameters).toString();
            }


            @Override
            public CharSequence extraContext()
            {
                return new TextParameter(PARAM_PLACE_EXTRA, parameters).toString();
            }
        };
    }


    @Override
    public GeoLocation geoLocation()
    {
        return new UriGeoLocation(URI.create("geo:" + new Text(mUri.path()).toString()));
    }
}
