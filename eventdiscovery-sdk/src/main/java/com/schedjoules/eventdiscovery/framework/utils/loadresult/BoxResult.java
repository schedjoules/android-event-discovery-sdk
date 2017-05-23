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

package com.schedjoules.eventdiscovery.framework.utils.loadresult;

import android.os.Parcel;

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;


/**
 * A successful {@link LoadResult} that can take a {@link Box} and use it's content as result value.
 *
 * @author Gabor Keszthelyi
 */
public final class BoxResult<T> implements LoadResult<T>
{
    private final Box<T> mBox;


    public BoxResult(Box<T> box)
    {
        mBox = box;
    }


    @Override
    public boolean isSuccess()
    {
        return true;
    }


    @Override
    public T result() throws IllegalStateException
    {
        return mBox.content();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mBox, flags);
    }


    public static final Creator<BoxResult> CREATOR = new Creator<BoxResult>()
    {
        @Override
        public BoxResult createFromParcel(Parcel in)
        {
            return new BoxResult((Box) in.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public BoxResult[] newArray(int size)
        {
            return new BoxResult[size];
        }
    };
}
