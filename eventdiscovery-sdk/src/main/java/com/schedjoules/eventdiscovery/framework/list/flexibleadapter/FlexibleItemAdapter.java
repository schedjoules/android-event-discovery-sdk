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

package com.schedjoules.eventdiscovery.framework.list.flexibleadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.eventdiscovery.framework.list.ListItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * Adapts a {@link ListItem} to {@link IFlexible}.
 *
 * @author Gabor Keszthelyi
 */
public final class FlexibleItemAdapter<V extends View> extends AbstractFlexibleItem<SimpleFlexibleViewHolder<V>>
{
    private final ListItem<V> mListItem;


    public FlexibleItemAdapter(ListItem<V> listItem)
    {
        mListItem = listItem;
    }


    @Override
    public SimpleFlexibleViewHolder<V> createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent)
    {
        //noinspection unchecked
        V view = (V) inflater.inflate(mListItem.layoutResId(), parent, false);
        return new SimpleFlexibleViewHolder<>(view, adapter);
    }


    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SimpleFlexibleViewHolder<V> holder, int position, List payloads)
    {
        mListItem.bindDataTo(holder.mView);
    }


    @Override
    public int getLayoutRes()
    {
        return mListItem.layoutResId();
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

        FlexibleItemAdapter<?> that = (FlexibleItemAdapter<?>) o;

        return mListItem.equals(that.mListItem);

    }


    @Override
    public int hashCode()
    {
        return mListItem.hashCode();
    }


    @Override
    public String toString()
    {
        return "FlexibleItemAdapter{" +
                "mListItem=" + mListItem +
                '}';
    }
}
