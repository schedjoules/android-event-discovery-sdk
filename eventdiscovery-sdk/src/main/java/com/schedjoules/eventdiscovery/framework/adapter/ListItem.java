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

package com.schedjoules.eventdiscovery.framework.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Represents an item in an {@link RecyclerView}, used by {@link GeneralMultiTypeAdapter}.
 *
 * @author Gabor Keszthelyi
 */
public interface ListItem<V extends View>
{
    /**
     * The layout for the View corresponding to this list item. Root of the layout must match &lt;V&gt; type
     */
    @LayoutRes
    int layoutResId();

    /**
     * If applicable, this method binds the data that belongs to this item to the item View, i.e. sets values and registers listeners on the View.
     *
     * @param view
     *         the item view
     */
    void bindDataTo(V view);

}
