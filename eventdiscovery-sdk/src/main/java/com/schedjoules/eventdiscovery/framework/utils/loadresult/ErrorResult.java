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


/**
 * Error {@link LoadResult}.
 *
 * @author Gabor Keszthelyi
 */
public final class ErrorResult<T> implements LoadResult<T>
{
    private final Exception mCauseException;


    public ErrorResult(Exception causeException)
    {
        mCauseException = causeException;
    }


    @Override
    public T result() throws LoadResultException
    {
        throw new LoadResultException(mCauseException);
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeSerializable(mCauseException);
    }


    public static final Creator<ErrorResult> CREATOR = new Creator<ErrorResult>()
    {
        @Override
        public ErrorResult createFromParcel(Parcel in)
        {
            return new ErrorResult((Exception) in.readSerializable());
        }


        @Override
        public ErrorResult[] newArray(int size)
        {
            return new ErrorResult[size];
        }
    };
}
