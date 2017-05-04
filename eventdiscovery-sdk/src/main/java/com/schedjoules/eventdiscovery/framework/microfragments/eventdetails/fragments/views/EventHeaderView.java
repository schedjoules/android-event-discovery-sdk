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

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesDetailsHeaderBinding;
import com.schedjoules.eventdiscovery.framework.common.StatusBar;
import com.schedjoules.eventdiscovery.framework.utils.SchedJoulesLinks;
import com.schedjoules.eventdiscovery.framework.utils.colors.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.colors.Transparent;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * Represents the event details header view.
 *
 * @author Marten Gajda
 */
public final class EventHeaderView implements SmartView<Event>
{
    private final SchedjoulesDetailsHeaderBinding mHeader;
    private final Activity mActivity;
    private final EventHeaderDateTimeView mDateTimeView;


    public EventHeaderView(Activity activity, SchedjoulesDetailsHeaderBinding header)
    {
        mActivity = activity;
        mHeader = header;
        mDateTimeView = new EventHeaderDateTimeView(header);
    }


    @Override
    public void update(Event event)
    {
        new StatusBar(mActivity).update(Transparent.INSTANCE);

        Glide.with(mActivity)
                .load(new SchedJoulesLinks(event.links()).bannerUri())
                .into(mHeader.schedjoulesEventDetailBanner);

        mHeader.schedjoulesEventDetailToolbarLayout.setTitle(event.title());
        // the expanded title is always white
        mHeader.schedjoulesEventDetailToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(mActivity, R.color.schedjoules_white));
        mHeader.schedjoulesEventDetailToolbarCollapsed.setTitle("");

        mDateTimeView.update(event);

        // set home icon of the collapsed toolbar
        Drawable arrow = DrawableCompat.wrap(ContextCompat.getDrawable(mActivity, R.drawable.abc_ic_ab_back_material)).mutate();
        DrawableCompat.setTint(arrow, new AttributeColor(mActivity, R.attr.schedjoules_appBarIconColor).argb());
        mHeader.schedjoulesEventDetailToolbarCollapsed.setNavigationIcon(arrow);
    }
}
