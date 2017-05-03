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

import com.schedjoules.client.eventsdiscovery.locations.GeoLocationText;
import com.schedjoules.eventdiscovery.framework.model.location.geoplace.GeoPlace;
import com.schedjoules.eventdiscovery.framework.model.recent.CharSequenceConverter;

import org.dmfs.rfc3986.Scheme;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.encoding.XWwwFormUrlEncoded;
import org.dmfs.rfc3986.parameters.ParameterType;
import org.dmfs.rfc3986.parameters.parametersets.BasicParameterList;
import org.dmfs.rfc3986.parameters.parametertypes.BasicParameterType;
import org.dmfs.rfc3986.parameters.valuetypes.TextValueType;
import org.dmfs.rfc3986.paths.StructuredPath;
import org.dmfs.rfc3986.queries.SimpleQuery;
import org.dmfs.rfc3986.schemes.StringScheme;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc3986.uris.OpaqueUri;
import org.dmfs.rfc3986.uris.Text;


/**
 * A {@link CharSequenceConverter} to convert {@link GeoPlace} from/to {@link CharSequence}s.
 *
 * @author Marten Gajda
 */
public final class GeoPlaceCharSequenceConverter implements CharSequenceConverter<GeoPlace>
{
    private final static Scheme GEO_SCHEME = new StringScheme("geo");
    public final static ParameterType<CharSequence> PARAM_PLACE_ID = new BasicParameterType<>("id", TextValueType.INSTANCE);
    public final static ParameterType<CharSequence> PARAM_PLACE_NAME = new BasicParameterType<>("name", TextValueType.INSTANCE);
    public final static ParameterType<CharSequence> PARAM_PLACE_EXTRA = new BasicParameterType<>("extra", TextValueType.INSTANCE);


    @Override
    public CharSequence fromValue(GeoPlace value)
    {
        return new Text(
                new OpaqueUri(GEO_SCHEME,
                        new StructuredPath(new Precoded(new GeoLocationText(value.geoLocation()))),
                        new SimpleQuery(
                                new XWwwFormUrlEncoded(
                                        new BasicParameterList(
                                                PARAM_PLACE_ID.parameter(value.namedPlace().id()),
                                                PARAM_PLACE_NAME.parameter(value.namedPlace().name()),
                                                PARAM_PLACE_EXTRA.parameter(value.namedPlace().extraContext()))))));
    }


    @Override
    public GeoPlace fromCharSequence(CharSequence chars)
    {
        return new UriGeoPlace(new LazyUri(new Precoded(chars)));
    }
}
