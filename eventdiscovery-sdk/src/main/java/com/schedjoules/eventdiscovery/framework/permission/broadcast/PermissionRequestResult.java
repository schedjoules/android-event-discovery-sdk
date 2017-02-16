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

package com.schedjoules.eventdiscovery.framework.permission.broadcast;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


/**
 * The data for the {@link Activity#onRequestPermissionsResult(int, String[], int[])} callback.
 *
 * @author Gabor Keszthelyi
 */
public final class PermissionRequestResult implements Parcelable
{
    public final int requestCode;

    @NonNull
    public final String[] permissions;

    @NonNull
    public final int[] grantResults;


    public PermissionRequestResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(requestCode);
        dest.writeStringArray(permissions);
        dest.writeIntArray(grantResults);
    }


    public static final Creator<PermissionRequestResult> CREATOR = new Creator<PermissionRequestResult>()
    {
        @Override
        public PermissionRequestResult createFromParcel(Parcel in)
        {
            return new PermissionRequestResult(in.readInt(), in.createStringArray(), in.createIntArray());
        }


        @Override
        public PermissionRequestResult[] newArray(int size)
        {
            return new PermissionRequestResult[size];
        }
    };
}
