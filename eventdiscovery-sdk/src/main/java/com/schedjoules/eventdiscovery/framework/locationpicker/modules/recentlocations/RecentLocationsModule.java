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

import android.app.Activity;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.TextHeaderItem;
import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.smart.Clickable;
import com.schedjoules.eventdiscovery.framework.locationpicker.listitems.PlaceSuggestionItem;
import com.schedjoules.eventdiscovery.framework.model.location.geoplace.GeoPlace;
import com.schedjoules.eventdiscovery.framework.model.recent.Recent;
import com.schedjoules.eventdiscovery.framework.model.recent.Recents;
import com.schedjoules.eventdiscovery.framework.model.recent.places.Prioritized;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.Clear;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ReplaceAll;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;

import java.util.ArrayList;
import java.util.List;


/**
 * {@link SearchModule} which shows the most recently selected locations.
 *
 * @author Gabor Keszthelyi
 */
public final class RecentLocationsModule implements SearchModule
{
    public static final SearchModuleFactory<GeoPlace> FACTORY = new SearchModuleFactory<GeoPlace>()
    {
        @Override
        public SearchModule create(Activity activity, ResultUpdateListener<ListItem> updateListener, ItemChosenAction<GeoPlace> itemSelectionAction)
        {
            return new RecentLocationsModule(
                    updateListener,
                    itemSelectionAction,
                    new RecentGeoPlaces(activity),
                    activity.getString(R.string.schedjoules_location_picker_caption_recent_locations));
        }
    };

    private final ResultUpdateListener<ListItem> mUpdateListener;
    private final ItemChosenAction<GeoPlace> mSelectionAction;
    private final CharSequence mHeaderTitle;
    private final Recents<GeoPlace> mRecents;


    private RecentLocationsModule(ResultUpdateListener<ListItem> updateListener,
                                  ItemChosenAction<GeoPlace> itemSelectionAction,
                                  Recents<GeoPlace> recents,
                                  CharSequence headerTitle)
    {
        mUpdateListener = updateListener;
        mSelectionAction = itemSelectionAction;
        mRecents = new Prioritized<>(recents);
        mHeaderTitle = headerTitle;
    }


    @Override
    public void shutDown()
    {
        // nothing to do
    }


    @Override
    public void onSearchQueryChange(final String newQuery)
    {
        if (!newQuery.isEmpty())
        {
            // TODO: maybe filter recent locations instead of hiding them all together
            mUpdateListener.onUpdate(new Clear<ListItem>(newQuery));
            return;
        }
        final List<ListItem> places = new ArrayList<>(10);
        int count = 0;
        for (final Recent<GeoPlace> recentPlace : mRecents)
        {
            ListItem listItem = new Clickable<>(
                    new PlaceSuggestionItem(recentPlace.value().namedPlace()),
                    new OnClickAction()
                    {
                        @Override
                        public void onClick()
                        {
                            mSelectionAction.onItemChosen(recentPlace.value());
                        }
                    }, "selectItem");
            places.add(listItem);
            if (count++ > 5)
            {
                break;
            }
        }

        if (places.size() > 0)
        {
            places.add(0, new TextHeaderItem(mHeaderTitle));
        }

        mUpdateListener.onUpdate(new ReplaceAll<>(places, newQuery));
    }
}
