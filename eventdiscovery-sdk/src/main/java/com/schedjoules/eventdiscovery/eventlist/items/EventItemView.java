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

package com.schedjoules.eventdiscovery.eventlist.items;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.schedjoules.eventdiscovery.R;


/**
 * View for the event list item.
 *
 * @author Gabor Keszthelyi
 */
public final class EventItemView extends RelativeLayout
{
    private TextView mTitle;
    private TextView mTime;
    private TextView mLocation;
    private TextView mCategory;
    private ImageView mThumbnail;


    public EventItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mTitle = (TextView) findViewById(R.id.schedjoules_event_item_title);
        mTime = (TextView) findViewById(R.id.schedjoules_event_item_time);
        mLocation = (TextView) findViewById(R.id.schedjoules_event_item_location);
        mCategory = (TextView) findViewById(R.id.schedjoules_event_item_category);
        mThumbnail = (ImageView) findViewById(R.id.schedjoules_event_item_thumbnail);
    }


    public void setTitle(CharSequence title)
    {
        mTitle.setText(title);
    }


    public void setTime(CharSequence formattedTime)
    {
        mTime.setText(formattedTime);
    }


    public void setLocation(CharSequence locationText)
    {
        mLocation.setText(locationText);
    }


    public void setCategory(CharSequence category)
    {
        mCategory.setText(category);
    }


    public void setThumbnail(@Nullable Uri uri)
    {
        if (uri != null)
        {
            // TODO not using Glide's lifecycle awareness here currently.
            // Glide.with(getContext()) uses the Activity actually and because it's destroyed, it crashes.
            // Glide.with(fragment) should be used but how to get that here?
            Glide.with(getContext().getApplicationContext())
                    .load(uri)
                /*
                 Note: Using simply .error(R.drawable.schedjoules_empty_thumbnail) doesn't work because of the vector drawable involved (crash on pre-Lollipop).
                 getResources().getDrawable(R.drawable.schedjoules_empty_thumbnail) can be used but that creates the Drawable always unnecessarily,
                 sometimes taking a few milliseconds.
                  */
                    .error(R.color.schedjoules_empty_thumbnail_background)
                    .into(mThumbnail);
        }
        else
        {
            Glide.clear(mThumbnail);
            mThumbnail.setImageResource(R.drawable.schedjoules_empty_thumbnail);
        }
    }
}