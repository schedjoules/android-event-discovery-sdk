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

package com.schedjoules.eventdiscovery.framework.location.recentlocations;

import android.app.Activity;
import android.content.Context;

import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;


/**
 * A decorator to {@link SearchModuleFactory}s of {@link GeoPlace}s that adds the selected {@link GeoPlace} to the recent locations.
 *
 * @author Marten Gajda
 */
public final class Remembered implements SearchModuleFactory<GeoPlace>
{
    private final SearchModuleFactory<GeoPlace> mDelegate;


    public Remembered(SearchModuleFactory<GeoPlace> mDelegate)
    {
        this.mDelegate = mDelegate;
    }


    @Override
    public SearchModule create(final Activity activity, ResultUpdateListener<ListItem> updateListener, final ItemChosenAction<GeoPlace> itemSelectionAction)
    {
        final Context context = activity.getApplicationContext();
        return mDelegate.create(activity, updateListener, new ItemChosenAction<GeoPlace>()
        {
            @Override
            public void onItemChosen(GeoPlace itemData)
            {
                new RecentGeoPlaces(context).recent(itemData).remember();
                itemSelectionAction.onItemChosen(itemData);
            }
        });
    }
}
