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

import android.os.Build;
import android.widget.Button;

import com.schedjoules.eventdiscovery.framework.utils.colors.Color;


/**
 * Util class for backward compatible, {@link Button} related methods.
 *
 * @author Gabor Keszthelyi
 */
public final class ButtonCompat
{
    public static void setBackgroundTint(Button button, Color color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            button.setBackground(TintedDrawable.tinted(button.getBackground(), color));
        }
        else
        {
            button.setBackgroundDrawable(TintedDrawable.tinted(button.getBackground(), color));
        }
    }
}

