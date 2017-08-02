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

import android.app.Activity;
import android.support.annotation.AttrRes;
import android.util.TypedValue;

import com.schedjoules.eventdiscovery.framework.utils.factory.AbstractLazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;


/**
 * Access to a theme-dependent drawable as resource id.
 *
 * @author Gabor Keszthelyi
 */
public final class ThemeDrawableResource extends AbstractLazy<Integer>
{
    public ThemeDrawableResource(final Activity activity, @AttrRes final int attrResId)
    {
        super(new Factory<Integer>()
        {
            @Override
            public Integer create()
            {
                TypedValue typedValue = new TypedValue();
                activity.getTheme().resolveAttribute(attrResId, typedValue, true);
                return typedValue.resourceId;
            }
        });
    }
}
