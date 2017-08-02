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

package com.schedjoules.eventdiscovery.framework.serialization.boxes;

import android.os.Parcel;

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;


/**
 * {@link Box} for a {@link Boolean} value.
 *
 * @author Gabor Keszthelyi
 */
public final class BooleanBox implements Box<Boolean>
{
    private final boolean mContent;


    public BooleanBox(boolean content)
    {
        mContent = content;
    }


    @Override
    public Boolean content()
    {
        return mContent;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mContent ? 1 : 0);
    }


    public static final Creator<BooleanBox> CREATOR = new Creator<BooleanBox>()
    {
        @Override
        public BooleanBox createFromParcel(Parcel in)
        {
            return new BooleanBox(in.readInt() == 1);
        }


        @Override
        public BooleanBox[] newArray(int size)
        {
            return new BooleanBox[size];
        }
    };
}
