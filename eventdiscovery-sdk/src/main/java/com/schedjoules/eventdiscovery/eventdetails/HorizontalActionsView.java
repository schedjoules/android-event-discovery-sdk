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

package com.schedjoules.eventdiscovery.eventdetails;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    private ProgressBar mLoadingIndicator;
    private LinearLayout mActionsHolder;


    public HorizontalActionsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        mLoadingIndicator = (ProgressBar) findViewById(R.id.schedjoules_event_details_horizontal_actions_progressbar);
        mActionsHolder = (LinearLayout) findViewById(R.id.schedjoules_event_horizontal_actions_holder);

        mLoadingIndicator.setAlpha(0);
        mLoadingIndicator.animate().alpha(1f).setStartDelay(1000).setDuration(500);
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

        crossfade();
    }


    private Space space(float weight)
    {
        Space space = new Space(getContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(0, 1, weight));
        return space;
    }


    // From https://developer.android.com/training/animation/crossfade.html
    private void crossfade()
    {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        mActionsHolder.setAlpha(0f);
        mActionsHolder.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        mActionsHolder.animate()
                .alpha(1f)
                .setDuration(400)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        mLoadingIndicator.animate()
                .alpha(0f)
                .setStartDelay(0)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        mLoadingIndicator.setVisibility(View.GONE);
                    }
                });
    }
}
