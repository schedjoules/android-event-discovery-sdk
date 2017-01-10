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

package com.schedjoules.eventdiscovery.framework.location.model;

import android.content.Context;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.model.UndefinedGeoLocation;


/**
 * {@link GeoPlace} that indicates that no location has been selected yet or it has been cleared.
 *
 * @author Gabor Keszthelyi
 */
public final class Anywhere implements GeoPlace
{
    private final NamedPlace mAnywhereNamedPlace;


    public Anywhere(Context context)
    {
        mAnywhereNamedPlace = new AnywhereNamedPlace(
                context.getString(R.string.schedjoules_default_location_placeholder_name));
    }


    @Override
    public NamedPlace namedPlace()
    {
        return mAnywhereNamedPlace;
    }


    @Override
    public GeoLocation geoLocation()
    {
        return UndefinedGeoLocation.INSTANCE;
    }


    private static class AnywhereNamedPlace implements NamedPlace
    {
        private final CharSequence mName;


        private AnywhereNamedPlace(CharSequence name)
        {
            mName = name;
        }


        @Override
        public String id()
        {
            throw new UnsupportedOperationException(
                    String.format("%s doesn't have id, just name", Anywhere.class.getName()));
        }


        @Override
        public CharSequence name()
        {
            return mName;
        }


        @Override
        public CharSequence extraContext()
        {
            throw new UnsupportedOperationException(
                    String.format("%s doesn't have extraContext, just name", Anywhere.class.getName()));
        }
    }
}
