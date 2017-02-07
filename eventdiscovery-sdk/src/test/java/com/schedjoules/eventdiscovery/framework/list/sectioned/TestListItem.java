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

import android.view.View;

import com.schedjoules.eventdiscovery.framework.list.ListItem;


/**
 * {@link ListItem} that can be used in tests.
 *
 * @author Gabor Keszthelyi
 */
public class TestListItem implements ListItem
{
    private final int mId;


    public static ListItem testItem(int id)
    {
        return new TestListItem(id);
    }


    public TestListItem(int id)
    {
        mId = id;
    }


    @Override
    public int layoutResId()
    {
        throw new RuntimeException("Should not be called");
    }


    @Override
    public void bindDataTo(View view)
    {
        throw new RuntimeException("Should not be called");
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        TestListItem that = (TestListItem) o;

        return mId == that.mId;

    }


    @Override
    public int hashCode()
    {
        return mId;
    }


    @Override
    public String toString()
    {
        return "TestListItem{" +
                "mId=" + mId +
                '}';
    }
}
