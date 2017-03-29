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

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.widgets.LinkifyingExpandableTextView;


/**
 * {@link View} for an event detail item that has expandable text content.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsExpandableItemView extends RelativeLayout
{
    private LinkifyingExpandableTextView mTitleView;
    private ImageView mIcon;


    public EventDetailsExpandableItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    public static EventDetailsExpandableItemView inflate(ViewGroup parent)
    {
        return (EventDetailsExpandableItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedjoules_view_event_details_expandable_item, parent, false);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mIcon = (ImageView) findViewById(R.id.schedjoules_event_details_item_icon);
        mTitleView = new LinkifyingExpandableTextView((ExpandableTextView) findViewById(R.id.schedjoules_event_details_item_title));
    }


    public void setText(String text)
    {
        mTitleView.update(text);
    }


    public void setIcon(@DrawableRes int iconResId)
    {
        mIcon.setImageResource(iconResId);
    }

}
