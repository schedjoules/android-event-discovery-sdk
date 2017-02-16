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

package com.schedjoules.eventdiscovery.framework.location.tasks;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.framework.async.CachingSafeAsyncTask;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.StructuredGeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.StructuredNamedPlace;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.NamedPlace;
import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.cache.Cache;

import java.util.List;


/**
 * {@link AsyncTask} to get the current city and country name using {@link Geocoder} given a geo location.
 *
 * @author Gabor Keszthelyi
 */
public final class GetCityTask<GL extends GeoLocation & Equalable> extends CachingSafeAsyncTask<GL, Geocoder, Void, GeoPlace>
{

    public GetCityTask(GL location, Client<GL> client, Cache<GL, GeoPlace> cache)
    {
        super(location, client, cache);
    }


    @Override
    protected GeoPlace doInBackgroundWithExceptionForNonCached(GL location, Geocoder geocoder) throws Exception
    {
        List<Address> addresses = geocoder.getFromLocation((double) location.latitude(), (double) location.longitude(), 1);

        if (addresses == null || addresses.isEmpty())
        {
            throw new RuntimeException("No address returned by Geocoder.");
        }

        Address address = addresses.get(0);
        String id = String.format("LocatedCity-%s-%s-%s",
                address.getCountryCode(), address.getLocality(), location.toString());
        NamedPlace namedPlace = new StructuredNamedPlace(id, address.getLocality(), address.getCountryName());
        return new StructuredGeoPlace(namedPlace, location);
    }


    public interface Client<GL extends GeoLocation & Equalable> extends SafeAsyncTaskCallback<GL, GeoPlace>
    {

    }
}
