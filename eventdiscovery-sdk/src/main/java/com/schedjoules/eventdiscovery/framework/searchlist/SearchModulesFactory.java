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

package com.schedjoules.eventdiscovery.framework.searchlist;

import android.app.Activity;

import com.schedjoules.eventdiscovery.framework.list.ListItemSelectionAction;
import com.schedjoules.eventdiscovery.framework.utils.Factory;

import java.util.ArrayList;
import java.util.List;


/**
 * Factory for creating {@link SearchModule}s for a search list.
 *
 * @author Gabor Keszthelyi
 */
public final class SearchModulesFactory<ITEM_DATA> implements Factory<List<SearchModule>>
{
    private final Activity mActivity;
    private final SearchListItems mSearchListItems;
    private final ListItemSelectionAction<ITEM_DATA> mItemSelectionAction;
    private final List<SearchModuleFactory<ITEM_DATA>> mFactories;


    public SearchModulesFactory(Activity activity,
                                SearchListItems searchListItems,
                                ListItemSelectionAction<ITEM_DATA> itemSelectionAction,
                                List<SearchModuleFactory<ITEM_DATA>> factories)
    {
        mActivity = activity;
        mSearchListItems = searchListItems;
        mItemSelectionAction = itemSelectionAction;
        mFactories = factories;
    }


    @Override
    public List<SearchModule> create()
    {
        List<SearchModule> modules = new ArrayList<>(mFactories.size());

        for (int i = 0; i < mFactories.size(); i++)
        {
            SearchModule searchModule = mFactories.get(i).create(
                    mActivity,
                    // Adding 1000 to the index to avoid confusion with list positions
                    new SectionedResultUpdateListenerAdapter<>(i + 1000, mSearchListItems),
                    mItemSelectionAction);
            modules.add(searchModule);
        }
        return modules;
    }
}
