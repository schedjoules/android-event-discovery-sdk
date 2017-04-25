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

package com.schedjoules.eventdiscovery.framework.list.sectioned;

import android.support.annotation.NonNull;
import android.view.View;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.equalables.Composite;


/**
 * Basic implementation for {@link SectionableListItem}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicSectionableListItem<V extends View> implements SectionableListItem<V>
{
    private final ListItem<V> mDelegate;
    private final int mSectionNumber;
    private final Equalable mId;


    public BasicSectionableListItem(ListItem<V> delegate, int sectionNumber)
    {
        mDelegate = delegate;
        mSectionNumber = sectionNumber;
        mId = new Composite(delegate.id(), sectionNumber);
    }


    @Override
    public int sectionNumber()
    {
        return mSectionNumber;
    }


    @Override
    public ListItem<V> item()
    {
        return mDelegate;
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
    }


    @NonNull
    @Override
    public Equalable id()
    {
        return mId;
    }


    @Override
    public String toString()
    {
        return "BasicSectionableListItem{" +
                "mDelegate=" + mDelegate +
                ", mSectionNumber=" + mSectionNumber +
                '}';
    }
}
