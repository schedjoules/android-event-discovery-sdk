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

import android.support.v7.widget.RecyclerView;
import android.view.View;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;


/**
 * {@link RecyclerView.ViewHolder} for {@link IHeader}.
 *
 * @author Gabor Keszthelyi
 */
public final class HeaderFlexibleViewHolder<V extends View> extends FlexibleViewHolder
{

    public final V mView;


    public HeaderFlexibleViewHolder(V view, FlexibleAdapter adapter)
    {
        super(view, adapter, true);
        mView = view;
    }
}
