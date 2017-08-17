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
import android.widget.TextView;

import com.schedjoules.eventdiscovery.framework.utils.colors.AccentColor;
import com.schedjoules.eventdiscovery.framework.utils.colors.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.colors.Color;
import com.schedjoules.eventdiscovery.framework.utils.colors.Transparent;
import com.schedjoules.eventdiscovery.framework.utils.colors.White;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * {@link SmartView} for {@link TextView} that can be highlighted,
 * i.e. when not highlighted: its background tinted with white, text color {@link android.R.attr#textColorPrimary},
 * when not highlighted: its background tinted with accent color, text color is white.
 *
 * @author Gabor Keszthelyi
 */
public final class Highlightable implements SmartView<Boolean>
{
    private final TextView mTextView;


    public Highlightable(TextView textView)
    {
        mTextView = textView;
    }


    @Override
    public void update(Boolean selected)
    {
        Context context = mTextView.getContext();

        Color backgroundColor = selected ? new AccentColor(context) : Transparent.INSTANCE;
        new BackgroundTintable(mTextView).update(backgroundColor);

        Color textColor = selected ? White.INSTANCE : new AttributeColor(context, android.R.attr.textColorPrimary);
        mTextView.setTextColor(textColor.argb());
        // for some reason (maybe an Android bug?) the "all" view didn't update in the dark theme.
        // calling invalidate explicitly seems to fix it.
        mTextView.invalidate();
    }
}
