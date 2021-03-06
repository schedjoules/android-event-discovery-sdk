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

package com.schedjoules.eventdiscovery.framework.filter.views;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.filter.filterstate.FilterState;
import com.schedjoules.eventdiscovery.framework.utils.ContextActivity;
import com.schedjoules.eventdiscovery.framework.utils.ThemeDrawableResource;
import com.schedjoules.eventdiscovery.framework.utils.colors.AccentColor;
import com.schedjoules.eventdiscovery.framework.utils.colors.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.colors.Color;
import com.schedjoules.eventdiscovery.framework.utils.colors.ResourceColor;
import com.schedjoules.eventdiscovery.framework.utils.colors.Transparent;
import com.schedjoules.eventdiscovery.framework.utils.colors.White;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;
import com.schedjoules.eventdiscovery.framework.widgets.BackgroundTintable;
import com.schedjoules.eventdiscovery.framework.widgets.TextWithIcon;


/**
 * Represents the View for an event filter title.
 *
 * @author Gabor Keszthelyi
 */
public final class FilterTitleView implements SmartView<FilterState>
{
    private final TextView mTitleView;
    private final int mTitleText;


    public FilterTitleView(TextView titleView, @StringRes int titleText)
    {
        mTitleView = titleView;
        mTitleText = titleText;
    }


    @Override
    public void update(FilterState filterState)
    {
        Context context = mTitleView.getContext();

        mTitleView.setTextColor(textColor(filterState, context).argb());

        new BackgroundTintable(mTitleView).update(backgroundColor(filterState, context));

        mTitleView.setText(new TextWithIcon(context, context.getString(mTitleText), arrowId(filterState, context)));
    }


    private Color textColor(FilterState filterState, Context context)
    {
        return filterState.hasSelection() ? White.INSTANCE : new AttributeColor(context, android.R.attr.textColorPrimary);
    }


    private Color backgroundColor(FilterState filterState, Context context)
    {
        if (filterState.hasSelection())
        {
            return new AccentColor(context);
        }
        else if (filterState.isExpanded())
        {
            return new ResourceColor(context, R.color.schedjoules_filter_bg_expanded);
        }
        else
        {
            return Transparent.INSTANCE;
        }
    }


    private int arrowId(FilterState filterState, Context context)
    {
        if (filterState.isExpanded())
        {
            return filterState.hasSelection() ?
                    R.drawable.schedjoules_ic_arrow_drop_up_white_16sp
                    :
                    new ThemeDrawableResource(new ContextActivity(context).get(), R.attr.schedjoules_dropUpArrow_16sp).get();
        }
        else
        {
            return filterState.hasSelection() ?
                    R.drawable.schedjoules_ic_arrow_drop_down_white_16sp
                    :
                    new ThemeDrawableResource(new ContextActivity(context).get(), R.attr.schedjoules_dropdownArrow_16sp).get();
        }
    }

}
