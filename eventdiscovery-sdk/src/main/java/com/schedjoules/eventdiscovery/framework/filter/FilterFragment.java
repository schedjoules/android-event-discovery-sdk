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

package com.schedjoules.eventdiscovery.framework.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.eventsdiscovery.Category;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.filter.views.EventFilterView;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.CategoryBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.IterableBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.FragmentBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;

import org.dmfs.pigeonpost.Cage;


/**
 * Fragment for the filter on the list screen.
 *
 * @author Gabor Keszthelyi
 */
public final class FilterFragment extends BaseFragment implements CategorySelectionChangeListener
{

    private Cage<Box<Iterable<Category>>> mCategoriesCage;


    public static Fragment newInstance(Cage<Box<Iterable<Category>>> categoriesCage)
    {
        return new FragmentBuilder(new FilterFragment())
                .with(Keys.CATEGORIES_CAGE, new ParcelableBox<>(categoriesCage))
                .build();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        EventFilterView filterView = (EventFilterView) inflater.inflate(R.layout.schedjoules_fragment_event_list_filter, container, false);
        filterView.listen(this);
        mCategoriesCage = new Argument<>(Keys.CATEGORIES_CAGE, this).get();
        return filterView;
    }


    @Override
    public void onCategorySelectionChanged(Iterable<Category> selectedCategories)
    {
        mCategoriesCage.pigeon(new IterableBox<>(selectedCategories, CategoryBox.FACTORY)).send(getContext());
    }
}
