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

import android.Manifest;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.searchlist.QueryPredicate;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.permissionproxy.AbstractPermissionProxyModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.permissionproxy.PermissionProxy;


/**
 * {@link SearchModuleFactory} that decorates {@link CurrentLocationModule} with a {@link PermissionProxy} for {@link
 * Manifest.permission#ACCESS_FINE_LOCATION}.
 *
 * @author Gabor Keszthelyi
 */
public final class CurrentLocationPermissionProxyFactory extends AbstractPermissionProxyModuleFactory<GeoPlace>
{

    public CurrentLocationPermissionProxyFactory(SearchModuleFactory<GeoPlace> delegateModuleFactory)
    {
        super(delegateModuleFactory,
                new EmptyQueryPredicate(),
                Manifest.permission.ACCESS_FINE_LOCATION,
                R.string.schedjoules_location_picker_current_location,
                R.string.schedjoules_location_picker_current_location_permission_rationale,
                R.string.schedjoules_location_picker_current_location_permission_inform_about_phone_settings);
    }


    private static class EmptyQueryPredicate implements QueryPredicate
    {

        @Override
        public boolean isValid(String query)
        {
            return query.isEmpty();
        }
    }
}
