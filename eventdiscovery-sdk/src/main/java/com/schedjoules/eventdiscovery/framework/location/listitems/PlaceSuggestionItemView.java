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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.NamedPlace;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * View for the place suggestion items in the list.
 *
 * @author Gabor Keszthelyi
 */
public final class PlaceSuggestionItemView<T extends NamedPlace> extends RelativeLayout implements SmartView<T>
{

    private TextView mName;
    private TextView mExtraContext;


    public PlaceSuggestionItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mName = (TextView) findViewById(R.id.schedjoules_place_suggestion_item_name);
        mExtraContext = (TextView) findViewById(R.id.schedjoules_location_suggestion_item_extra_context);
    }


    @Override
    public void update(T namedPlace)
    {
        mName.setText(namedPlace.name());
        mExtraContext.setText(namedPlace.extraContext());
    }

}
