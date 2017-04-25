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

package com.schedjoules.eventdiscovery.framework.location.listitems;

import android.support.annotation.NonNull;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.NamedPlace;
import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.equalables.LazyObjectEqualable;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;


/**
 * {@link ListItem} for the place suggestion items on the location picker.
 *
 * @author Gabor Keszthelyi
 */
public final class PlaceSuggestionItem implements ListItem<PlaceSuggestionItemView>
{
    private final NamedPlace mNamedPlace;

    private final Equalable mId;


    public PlaceSuggestionItem(final NamedPlace namedPlace)
    {
        mNamedPlace = namedPlace;

        mId = new LazyObjectEqualable(new Factory<Object>()
        {
            @Override
            public Object create()
            {
                return namedPlace.id();
            }
        });
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_place_suggestion;
    }


    @Override
    public void bindDataTo(PlaceSuggestionItemView view)
    {
        view.update(mNamedPlace);
    }


    @NonNull
    @Override
    public Equalable id()
    {
        return mId;
    }


    @Override
    public String toString()
    {
        return "PlaceSuggestionItem{" +
                "mNamedPlace=" + mNamedPlace +
                '}';
    }
}
