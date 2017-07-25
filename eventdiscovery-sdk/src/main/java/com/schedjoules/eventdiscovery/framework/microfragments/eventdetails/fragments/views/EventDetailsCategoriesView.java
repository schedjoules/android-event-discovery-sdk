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
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schedjoules.client.eventsdiscovery.Category;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewEventDetailsCategoriesBinding;
import com.schedjoules.eventdiscovery.framework.common.CategoriesCache;
import com.schedjoules.eventdiscovery.framework.model.category.EventCategories;
import com.schedjoules.eventdiscovery.framework.utils.ContextActivity;
import com.schedjoules.eventdiscovery.framework.utils.TintedDrawable;
import com.schedjoules.eventdiscovery.framework.utils.colors.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;

import java.util.Iterator;


/**
 * Represents the View for the category labels on the details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsCategoriesView implements SmartView<Event>
{
    private final ViewGroup mHolderGroup;


    public EventDetailsCategoriesView(SchedjoulesViewEventDetailsCategoriesBinding binding)
    {
        mHolderGroup = binding.schedjoulesEventDetailsCategoriesGroup;
    }


    @Override
    public void update(Event event)
    {
        Iterator<Category> categories = new EventCategories(event, new CategoriesCache(new ContextActivity(mHolderGroup).get()));
        Context context = mHolderGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // hide the group if no categories are available
        mHolderGroup.setVisibility(categories.hasNext() ? View.VISIBLE : View.GONE);

        while (categories.hasNext())
        {
            TextView labelView = (TextView) inflater.inflate(R.layout.schedjoules_view_event_details_category, null);

            labelView.setText(categories.next().label());

            Drawable background = new TintedDrawable(context,
                    R.drawable.schedjoules_filled_bg_w_rounded_corner,
                    new AttributeColor(context, R.attr.colorAccent)).get();

            ViewCompat.setBackground(labelView, background);

            mHolderGroup.addView(labelView);
        }
    }

}
