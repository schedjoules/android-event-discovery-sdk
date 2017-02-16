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

import android.view.View;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.location.model.NamedPlace;


/**
 * List item for the location suggestion.
 *
 * @author Gabor Keszthelyi
 */
public final class PlaceSuggestionItem implements ListItem<PlaceSuggestionItemView>
{
    private final NamedPlace mNamedPlace;

    private OnClickListener mOnClickListener;


    public PlaceSuggestionItem(NamedPlace namedPlace)
    {
        mNamedPlace = namedPlace;
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
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mOnClickListener.onPlaceSuggestionSelected(mNamedPlace);
            }
        });
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        PlaceSuggestionItem that = (PlaceSuggestionItem) o;

        return mNamedPlace.id().equals(that.mNamedPlace.id());

    }


    @Override
    public int hashCode()
    {
        return mNamedPlace.id().hashCode();
    }


    public void setListener(OnClickListener listener)
    {
        mOnClickListener = listener;
    }


    @Override
    public String toString()
    {
        return "PlaceSuggestionItem{" +
                "mNamedPlace=" + mNamedPlace.name() +
                '}';
    }


    public interface OnClickListener
    {
        void onPlaceSuggestionSelected(NamedPlace namedPlace);
    }
}
