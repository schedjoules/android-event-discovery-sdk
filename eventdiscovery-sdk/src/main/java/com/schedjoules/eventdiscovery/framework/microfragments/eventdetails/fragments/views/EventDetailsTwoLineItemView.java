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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.views;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;


/**
 * {@link View} for the event details items listed horizontally on the details screen with a icon, title and subtitle.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsTwoLineItemView extends RelativeLayout
{
    private ImageView mIcon;
    private TextView mTitle;
    private TextView mSubTitle;


    public EventDetailsTwoLineItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    public static EventDetailsTwoLineItemView inflate(ViewGroup parent)
    {
        return (EventDetailsTwoLineItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedjoules_view_event_details_item_twoline, parent, false);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mIcon = (ImageView) findViewById(R.id.schedjoules_event_details_item_icon);
        mTitle = (TextView) findViewById(R.id.schedjoules_event_details_item_title);
        mSubTitle = (TextView) findViewById(R.id.schedjoules_event_details_item_subtitle);
    }


    public void setIcon(@DrawableRes int iconResId)
    {
        mIcon.setImageResource(iconResId);
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
