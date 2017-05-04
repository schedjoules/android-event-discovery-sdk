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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.utils.TintedDrawable;
import com.schedjoules.eventdiscovery.framework.utils.colors.AttributeColor;


/**
 * {@link ProgressBar} that tints the indeterminate drawable with accent color on pre-Lollipop versions.
 *
 * @author Gabor Keszthelyi
 */
public final class AccentColoredProgressBar extends ProgressBar
{
    public AccentColoredProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        if (Build.VERSION.SDK_INT < 21)
        {
            tintIndeterminateDrawable();
        }
    }


    private void tintIndeterminateDrawable()
    {
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable != null)
        {
            setIndeterminateDrawable(TintedDrawable.tinted(indeterminateDrawable, new AttributeColor(getContext(), R.attr.colorAccent)));
        }
    }
}
