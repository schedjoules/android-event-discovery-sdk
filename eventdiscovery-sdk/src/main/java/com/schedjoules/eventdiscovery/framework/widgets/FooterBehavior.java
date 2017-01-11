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

package com.schedjoules.eventdiscovery.framework.widgets;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;


/**
 * A {@link CoordinatorLayout.Behavior} that emulates a footer when a {@link CollapsingToolbarLayout} is present.
 * <p>
 * The behavior translates an view anchored to a placeholder underneath the content to stick to the bottom of the {@link CoordinatorLayout} if the placeholder
 * is above.
 *
 * @author Marten Gajda
 */
public final class FooterBehavior extends CoordinatorLayout.Behavior<View>
{
    public FooterBehavior(Context context, AttributeSet attributes)
    {
        super(context, attributes);
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency)
    {
        int[] depLocation = new int[2];
        dependency.getLocationInWindow(depLocation);
        int dependencyBottom = depLocation[1] + dependency.getHeight();
        int[] parentLocation = new int[2];
        parent.getLocationInWindow(parentLocation);
        int parentBottom = parentLocation[1] + parent.getHeight();

        if (dependencyBottom < parentBottom)
        {
            child.setTranslationY(parentBottom - dependencyBottom);
        }
        else
        {
            child.setTranslationY(dependencyBottom - parentBottom);
        }

        return false;
    }
}
