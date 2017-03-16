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

package com.schedjoules.eventdiscovery.framework.list.smart;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * Abstract class for {@link ListItem}s that work with {@link SmartView}s.
 * <p>
 * Note: Usually list items' 'equality' should be based on the data/model objects (type D) equality, so make sure that the data type equality is defined.
 *
 * @author Gabor Keszthelyi
 */
public abstract class AbstractSmartListItem<D, V extends View & SmartView<D>> implements ListItem<V>
{
    private final D mItemData;
    private final int mLayout;


    public AbstractSmartListItem(D itemData, @LayoutRes int layout)
    {
        mItemData = itemData;
        mLayout = layout;
    }


    @Override
    public final int layoutResId()
    {
        return mLayout;
    }


    @Override
    public final void bindDataTo(V view)
    {
        view.update(mItemData);
    }


    @Override
    public final boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        AbstractSmartListItem<?, ?> that = (AbstractSmartListItem<?, ?>) o;

        return mItemData.equals(that.mItemData);

    }


    @Override
    public final int hashCode()
    {
        return mItemData.hashCode();
    }


    @Override
    public final String toString()
    {
        return toStringLabel() + "{" +
                "itemData=" + mItemData +
                '}';
    }


    /**
     * Label that is added to the toString() value of the object, practically use the class name in string literal, like "MyListItem".
     */
    protected abstract String toStringLabel();
}
