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

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;


/**
 * {@link ListItem} decorator that can set up a {@link OnClickAction} linked to specific child view of the related View.
 *
 * @author Gabor Keszthelyi
 */
public final class SubClickable<V extends View> implements ListItem<V>
{

    private final ListItem<V> mDelegate;
    private final int mChildViewId;
    private final OnClickAction mOnClickAction;


    public SubClickable(ListItem<V> delegate, @IdRes int childViewId, OnClickAction onClickAction)
    {
        mDelegate = delegate;
        mChildViewId = childViewId;
        mOnClickAction = onClickAction;
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
        view.findViewById(mChildViewId).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mOnClickAction.onClick();
            }
        });
    }

}
