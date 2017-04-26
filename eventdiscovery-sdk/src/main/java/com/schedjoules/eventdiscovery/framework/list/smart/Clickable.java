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

import android.support.annotation.NonNull;
import android.view.View;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;


/**
 * {@link ListItem} decorator that sets up the given {@link OnClickAction} to be executed when the item View is clicked.
 *
 * @author Gabor Keszthelyi
 */
public final class Clickable<V extends View> implements ListItem<V>
{

    private final OnClickAction mOnClickAction;
    private final ListItem<V> mDelegate;


    public Clickable(ListItem<V> delegate, OnClickAction onClickAction)
    {
        mOnClickAction = onClickAction;
        mDelegate = delegate;
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
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mOnClickAction.onClick();
            }
        });
    }


    @NonNull
    @Override
    public Equalable id()
    {
        return mDelegate.id();
    }


    @Override
    public String toString()
    {
        return "Clickable{" +
                "mDelegate=" + mDelegate +
                '}';
    }
}
