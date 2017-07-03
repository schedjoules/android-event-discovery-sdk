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

import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;


/**
 * {@link Box} for an absent {@link Optional}.
 *
 * @author Gabor Keszthelyi
 */
public final class AbsentBox<T> implements Box<Optional<T>>
{

    @Override
    public Optional<T> content()
    {
        return Absent.absent();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {

    }


    public static final Creator<AbsentBox> CREATOR = new Creator<AbsentBox>()
    {
        @Override
        public AbsentBox createFromParcel(Parcel in)
        {
            return new AbsentBox();
        }


        @Override
        public AbsentBox[] newArray(int size)
        {
            return new AbsentBox[size];
        }
    };
}
