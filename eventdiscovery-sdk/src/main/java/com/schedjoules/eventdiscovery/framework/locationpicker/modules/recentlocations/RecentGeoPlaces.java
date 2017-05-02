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

package com.schedjoules.eventdiscovery.framework.locationpicker.modules.recentlocations;

import android.content.Context;

import com.schedjoules.eventdiscovery.framework.model.location.geoplace.GeoPlace;
import com.schedjoules.eventdiscovery.framework.model.recent.Recent;
import com.schedjoules.eventdiscovery.framework.model.recent.Recents;
import com.schedjoules.eventdiscovery.framework.model.recent.places.GeoPlaceCharSequenceConverter;
import com.schedjoules.eventdiscovery.framework.model.recent.places.SharedPreferencesRecents;

import java.util.Iterator;


/**
 * {@link Recents} of {@link GeoPlace}s.
 *
 * @author Marten Gajda
 */
public final class RecentGeoPlaces implements Recents<GeoPlace>
{
    private final Recents<GeoPlace> mDelegate;


    public RecentGeoPlaces(Context context)
    {
        mDelegate = new SharedPreferencesRecents<>(context, "locations", new GeoPlaceCharSequenceConverter());
    }


    @Override
    public Recent<GeoPlace> recent(GeoPlace object)
    {
        return mDelegate.recent(object);
    }


    @Override
    public Iterator<Recent<GeoPlace>> iterator()
    {
        return mDelegate.iterator();
    }
}
