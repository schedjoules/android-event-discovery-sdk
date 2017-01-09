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

package com.schedjoules.eventdiscovery.location.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;


/**
 * View for the location suggestion items in the list.
 *
 * @author Gabor Keszthelyi
 */
public final class LocationSuggestionItemView extends LinearLayout
{

    private TextView mTitle;
    private TextView mSubTitle;


    public LocationSuggestionItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mTitle = (TextView) findViewById(R.id.schedjoules_location_suggestion_item_title);
        mSubTitle = (TextView) findViewById(R.id.schedjoules_location_suggestion_item_subtitle);
    }


    public void setTitle(CharSequence title)
    {
        mTitle.setText(title);
    }


    public void setSubTitle(CharSequence subTitle)
    {
        mSubTitle.setText(subTitle);
    }
}
