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

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.State;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BoxFactory;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;


/**
 * {@link Box} for {@link ApiQuery}.
 *
 * @author Gabor Keszthelyi
 */
public final class ApiQueryBox<T> implements Box<ApiQuery<T>>
{
    private final ApiQuery<T> mApiQuery;


    public ApiQueryBox(ApiQuery<T> apiQuery)
    {
        mApiQuery = apiQuery;
    }


    @Override
    public ApiQuery content()
    {
        return mApiQuery;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeSerializable(mApiQuery.serializable());
    }


    public static final Creator<ApiQueryBox> CREATOR = new Creator<ApiQueryBox>()
    {
        @Override
        public ApiQueryBox createFromParcel(Parcel in)
        {
            return new ApiQueryBox<>(((State<ApiQuery>) in.readSerializable()).restored());
        }


        @Override
        public ApiQueryBox[] newArray(int size)
        {
            return new ApiQueryBox[size];
        }
    };


    // Note: method is needed instead of constant because of the generic parameter
    public static <T> BoxFactory<ApiQuery<T>> factory()
    {
        return new BoxFactory<ApiQuery<T>>()
        {
            @Override
            public Box<ApiQuery<T>> create(ApiQuery<T> value)
            {
                return new ApiQueryBox<T>(value);
            }
        };
    }

}
