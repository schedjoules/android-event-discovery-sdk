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

package com.schedjoules.eventdiscovery.framework.eventlist.items;

import android.support.annotation.NonNull;
import android.view.View;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.model.Equalable;


/**
 * Event list UI item for loading indicator.
 *
 * @author Gabor Keszthelyi
 */
public final class LoadingIndicatorItem implements ListItem<View>
{
    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_loading;
    }


    @Override
    public void bindDataTo(View view)
    {

    }


    @NonNull
    @Override
    public Equalable id()
    {
        throw new UnsupportedOperationException("ListItem id is not supported in Event List currently");
    }

}
