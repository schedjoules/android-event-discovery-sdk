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

package com.schedjoules.eventdiscovery.framework.location;

import android.content.Context;
import android.content.SharedPreferences;

import com.schedjoules.client.eventsdiscovery.locations.StructuredGeoLocation;
import com.schedjoules.eventdiscovery.framework.location.model.Anywhere;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.StructuredGeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.StructuredNamedPlace;


/**
 * {@link LastSelectedPlace} that uses SharedPreferences to store the data.
 *
 * @author Gabor Keszthelyi
 */
public final class SharedPrefLastSelectedPlace implements LastSelectedPlace
{
    private static final String PREFERENCES_NAME = "LastSelectedLocationStore";
    private static final String KEY_LOCATION_ID = "key_lastLocation_id";
    private static final String KEY_LOCATION_NAME = "key_lastLocation_name";
    private static final String KEY_LOCATION_EXTRA_CONTEXT = "key_lastLocation_extraContext";
    private static final String KEY_LATITUDE = "key_lastLocation_latitude";
    private static final String KEY_LONGITUDE = "key_lastLocation_longitude";

    private static final int NOT_EXIST = 6666;

    private final SharedPreferences mPrefs;
    private final Context mContext;


    public SharedPrefLastSelectedPlace(Context context)
    {
        mContext = context.getApplicationContext();
        mPrefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    @Override
    public void update(GeoPlace selectedPlace)
    {
        mPrefs.edit()
                .putString(KEY_LOCATION_ID, selectedPlace.namedPlace().id())
                .putString(KEY_LOCATION_NAME, selectedPlace.namedPlace().name().toString())
                .putString(KEY_LOCATION_EXTRA_CONTEXT, selectedPlace.namedPlace().extraContext().toString())
                .putFloat(KEY_LATITUDE, selectedPlace.geoLocation().latitude())
                .putFloat(KEY_LONGITUDE, selectedPlace.geoLocation().longitude())
                .apply();
    }


    @Override
    public GeoPlace get()
    {
        String id = mPrefs.getString(KEY_LOCATION_ID, null);
        String name = mPrefs.getString(KEY_LOCATION_NAME, null);
        String extraContext = mPrefs.getString(KEY_LOCATION_EXTRA_CONTEXT, null);
        float latitude = mPrefs.getFloat(KEY_LATITUDE, NOT_EXIST);
        float longitude = mPrefs.getFloat(KEY_LONGITUDE, NOT_EXIST);

        if (id == null || name == null || longitude == NOT_EXIST || latitude == NOT_EXIST)
        {
            return new Anywhere(mContext);
        }
        else
        {
            return new StructuredGeoPlace(
                    new StructuredNamedPlace(id, name, extraContext),
                    new StructuredGeoLocation(latitude, longitude));
        }
    }


    @Override
    public void clear()
    {
        mPrefs.edit().clear().apply();
    }
}
