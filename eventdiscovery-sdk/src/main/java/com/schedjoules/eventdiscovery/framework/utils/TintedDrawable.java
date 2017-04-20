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

package com.schedjoules.eventdiscovery.framework.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;


/**
 * Util method for creating tinted drawable.
 * <p>
 * Note: subclassing Drawable to create a Tinted decorator didn't work because of not being able to
 * delegate protected methods, like onBoundsChange(). It caused the drawn icon to be misplaced.
 *
 * @author Gabor Keszthelyi
 */
public final class TintedDrawable
{
    /**
     * Creates a tinted Drawable.
     */
    public static Drawable create(Context context, @DrawableRes int drawableResId, Color color)
    {
        return tint(ContextCompat.getDrawable(context, drawableResId), color);
    }


    /**
     * Returns a tinted version of the given drawable.
     */
    public static Drawable tint(Drawable drawable, Color color)
    {
        Drawable mutated = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(mutated, color.argb());
        return mutated;
    }


    private TintedDrawable()
    {

    }
}
