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

package com.schedjoules.eventdiscovery.framework.locationpicker.modules.placesuggestion;

import android.app.Activity;

import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApis;
import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.model.location.geoplace.GeoPlace;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;


/**
 * {@link SearchModuleFactory} for {@link PlaceSuggestionModule}.
 *
 * @author Gabor Keszthelyi
 */
public final class PlaceSuggestionModuleFactory implements SearchModuleFactory<GeoPlace>
{
    private final GoogleApis mGoogleApis;


    public PlaceSuggestionModuleFactory(GoogleApis googleApis)
    {
        mGoogleApis = googleApis;
    }


    @Override
    public SearchModule create(Activity activity, ResultUpdateListener<ListItem> updateListener, ItemChosenAction<GeoPlace> itemChosenAction)
    {
        return new PlaceSuggestionModule(activity, updateListener, itemChosenAction, mGoogleApis);
    }

}
