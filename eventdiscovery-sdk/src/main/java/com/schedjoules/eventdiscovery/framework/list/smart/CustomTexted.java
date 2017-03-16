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

package com.schedjoules.eventdiscovery.framework.list.smart;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.framework.list.ListItem;


/**
 * {@link ListItem} decorator that can set up custom text for a {@link TextView} child of the {@link View} corresponding to the delegate {@link ListItem}.
 * <p>
 * Note: {@link Button} is a {@link TextView} as well.
 *
 * @author Gabor Keszthelyi
 */
public final class CustomTexted<V extends View> implements ListItem<V>
{

    private final ListItem<V> mDelegate;
    private final int mTextViewId;
    private final CharSequence mCustomText;


    public CustomTexted(ListItem<V> delegate, @IdRes int textViewId, CharSequence customText)
    {
        mDelegate = delegate;
        mTextViewId = textViewId;
        mCustomText = customText;
    }


    @Override
    public int layoutResId()
    {
        return mDelegate.layoutResId();
    }


    @Override
    public void bindDataTo(V view)
    {
        mDelegate.bindDataTo(view);
        ((TextView) view.findViewById(mTextViewId)).setText(mCustomText);
    }

}
