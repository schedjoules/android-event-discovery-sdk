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

package com.schedjoules.eventdiscovery.microfragments.eventdetails.fragments.views;

import android.content.Context;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.schedjoules.eventdiscovery.R;


/**
 * {@link View} for the horizontal actions bar on the Event details screen.
 * <p>
 * Action views can be added dynamically, they are evenly spread out for the width of the screen.
 *
 * @author Gabor Keszthelyi
 */
public final class HorizontalActionsView extends RelativeLayout
{
    private LinearLayout mActionsHolder;


    public HorizontalActionsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        mActionsHolder = (LinearLayout) findViewById(R.id.schedjoules_event_horizontal_actions_holder);
    }


    public void showActionViews(Iterable<View> actionViews)
    {
        mActionsHolder.addView(space(1.4f));
        boolean first = true;
        for (View actionView : actionViews)
        {
            if (!first)
            {
                mActionsHolder.addView(space(2));
            }
            mActionsHolder.addView(actionView);
            first = false;
        }
        mActionsHolder.addView(space(1.4f));
    }


    private Space space(float weight)
    {
        Space space = new Space(getContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(0, 1, weight));
        return space;
    }
}
