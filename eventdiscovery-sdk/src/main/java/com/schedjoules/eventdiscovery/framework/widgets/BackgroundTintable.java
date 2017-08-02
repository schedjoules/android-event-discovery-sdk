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

import android.support.v4.view.ViewCompat;
import android.view.View;

import com.schedjoules.eventdiscovery.framework.utils.TintedDrawable;
import com.schedjoules.eventdiscovery.framework.utils.colors.Color;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * {@link SmartView} for Views whose backgrounds can be updated with a tint color.
 *
 * @author Gabor Keszthelyi
 */
public final class BackgroundTintable implements SmartView<Color>
{
    private final View mView;


    public BackgroundTintable(View view)
    {
        mView = view;
    }


    @Override
    public void update(Color color)
    {
        ViewCompat.setBackground(mView, new TintedDrawable(mView.getBackground(), color).get());
    }
}
