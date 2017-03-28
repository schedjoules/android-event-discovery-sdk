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

package com.schedjoules.eventdiscovery.framework.actions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.schedjoules.eventdiscovery.framework.utils.charsequence.CharSequenceFactory;
import com.schedjoules.eventdiscovery.framework.utils.charsequence.ResourceCharSequenceFactory;


/**
 * Basic general implementation for {@link Action} that can be 'configured' with the constructor parameters.
 *
 * @author Gabor Keszthelyi
 */
public final class SimpleAction implements Action
{
    private final CharSequenceFactory mLabelFactory;
    private final int mIconResId;
    private final ActionExecutable mActionExecutable;


    public SimpleAction(CharSequenceFactory labelFactory, @DrawableRes int iconResId, @NonNull ActionExecutable actionExecutable)
    {
        mLabelFactory = labelFactory;
        mIconResId = iconResId;
        mActionExecutable = actionExecutable;
    }


    public SimpleAction(@StringRes int labelResId, @DrawableRes int iconResId, @NonNull ActionExecutable actionExecutable)
    {
        this(new ResourceCharSequenceFactory(labelResId), iconResId, actionExecutable);
    }


    @NonNull
    @Override
    public CharSequence label(@NonNull Context context)
    {
        return mLabelFactory.create(context);
    }


    @NonNull
    @Override
    public Drawable icon(@NonNull Context context)
    {
        return ContextCompat.getDrawable(context, mIconResId);
    }


    @NonNull
    @Override
    public ActionExecutable actionExecutable()
    {
        return mActionExecutable;
    }
}
