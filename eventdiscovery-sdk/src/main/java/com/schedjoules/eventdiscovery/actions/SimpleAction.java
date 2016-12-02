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

package com.schedjoules.eventdiscovery.actions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;


/**
 * Basic general implementation for {@link Action} that can be 'configured' with the constructor parameters.
 *
 * @author Gabor Keszthelyi
 */
public final class SimpleAction implements Action
{
    private final String mRelType;
    private final int mLabelResId;
    private final int mIconResId;
    private final ActionExecutable mActionExecutable;


    public SimpleAction(@NonNull String relType, @StringRes int labelResId, @DrawableRes int iconResId, @NonNull ActionExecutable actionExecutable)
    {
        mRelType = relType;
        mLabelResId = labelResId;
        mIconResId = iconResId;
        mActionExecutable = actionExecutable;
    }


    @NonNull
    @Override
    public String shortLabel(@NonNull Context context)
    {
        return context.getString(mLabelResId);
    }


    @NonNull
    @Override
    public String longLabel(@NonNull Context context)
    {
        return context.getString(mLabelResId);
    }


    @NonNull
    @Override
    public Drawable icon(@NonNull Context context)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            return context.getResources().getDrawable(mIconResId);
        }
        return context.getDrawable(mIconResId);
    }


    @NonNull
    @Override
    public ActionExecutable actionExecutable()
    {
        return mActionExecutable;
    }
}
