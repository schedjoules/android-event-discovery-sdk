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

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.locations.StructuredGeoLocation;
import com.schedjoules.eventdiscovery.framework.permissions.AppPermissions;
import com.schedjoules.eventdiscovery.framework.permissions.BasicAppPermissions;

import org.dmfs.rfc3986.utils.Optional;

import java.util.List;
import java.util.NoSuchElementException;

import static android.content.Context.LOCATION_SERVICE;


/**
 * An optional last known geo location of the device.
 *
 * @author Marten Gajda
 */
public final class LastKnownLocation implements Optional<GeoLocation>
{
    private final Context mContext;
    private Location mLocation;


    public LastKnownLocation(Context context)
    {
        mContext = context.getApplicationContext();
    }


    private Location location()
    {
        if (mLocation == null)
        {
            AppPermissions appPermissions = new BasicAppPermissions(mContext);
            if (!appPermissions.forName(Manifest.permission.ACCESS_FINE_LOCATION).isGranted() &&
                    !appPermissions.forName(Manifest.permission.ACCESS_COARSE_LOCATION).isGranted())
            {
                // no permission to access location
                return null;
            }
            LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            List<String> locationProviders = locationManager.getProviders(true);

            for (String locationProvider : locationProviders)
            {
                //noinspection MissingPermission
                Location l = locationManager.getLastKnownLocation(locationProvider);
                if (l != null)
                {
                    mLocation = l;
                    break;
                }
            }
        }
        return mLocation;
    }


    @Override
    public boolean isPresent()
    {
        return location() != null;
    }


    @Override
    public GeoLocation value(GeoLocation defaultValue)
    {
        Location location = location();
        return location == null ? defaultValue : new StructuredGeoLocation((float) location.getLatitude(), (float) location.getLongitude());
    }


    @Override
    public GeoLocation value() throws NoSuchElementException
    {
        Location location = location();
        if (location == null)
        {
            throw new NoSuchElementException("No last location available.");
        }
        return new StructuredGeoLocation((float) location.getLatitude(), (float) location.getLongitude());
    }
}
