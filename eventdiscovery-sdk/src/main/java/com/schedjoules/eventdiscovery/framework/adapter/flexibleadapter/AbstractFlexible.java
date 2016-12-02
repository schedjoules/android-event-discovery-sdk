/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.framework.adapter.flexibleadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.eventdiscovery.framework.adapter.ListItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * Abstract adapter class between {@link IFlexible} and {@link ListItem}.
 *
 * @author Gabor Keszthelyi
 */
public abstract class AbstractFlexible<V extends View> implements IFlexible<SimpleFlexibleViewHolder>, ListItem<V>
{
    @Override
    public boolean isEnabled()
    {
        return true;
    }


    @Override
    public void setEnabled(boolean enabled)
    {

    }


    @Override
    public boolean isHidden()
    {
        return false;
    }


    @Override
    public void setHidden(boolean hidden)
    {

    }


    @Override
    public boolean isSelectable()
    {
        return false;
    }


    @Override
    public void setSelectable(boolean selectable)
    {

    }


    @Override
    public boolean isDraggable()
    {
        return false;
    }


    @Override
    public void setDraggable(boolean draggable)
    {

    }


    @Override
    public boolean isSwipeable()
    {
        return false;
    }


    @Override
    public void setSwipeable(boolean swipeable)
    {

    }


    @Override
    public int getLayoutRes()
    {
        return layoutResId();
    }


    @Override
    public SimpleFlexibleViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent)
    {
        return new SimpleFlexibleViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }


    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SimpleFlexibleViewHolder holder, int position, List payloads)
    {
        //noinspection unchecked
        bindDataTo((V) holder.mView);
    }
}
