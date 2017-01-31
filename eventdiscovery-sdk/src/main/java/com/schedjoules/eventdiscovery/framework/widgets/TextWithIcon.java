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
import android.support.annotation.DrawableRes;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ImageSpan;


/**
 * A {@link SpannableString} that appends an icon at the end of the provided {@link CharSequence}.
 * <p>
 * Note: Implementing {@link CharSequence} doesn't work, {@link Toolbar#setTitle(CharSequence)} works differently for {@link CharSequence} and {@link
 * SpannableString}.
 *
 * @author Gabor Keszthelyi
 */
public final class TextWithIcon extends SpannableString
{

    public TextWithIcon(Context context, CharSequence text, @DrawableRes int icon)
    {
        super(value(context, text, icon));
    }


    private static SpannableString value(Context context, CharSequence text, int icon)
    {
        ImageSpan imageSpan = new ImageSpan(context, icon);
        SpannableString span = new SpannableString(text + " ");
        span.setSpan(imageSpan, span.length() - 1, span.length(), 0);
        return span;
    }

}
