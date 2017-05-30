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

package com.schedjoules.eventdiscovery.framework.utils;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Util method to cause immediate deep parcelling of a {@link Parcelable}.
 * Can be used to test if {@link Parcelable} implementations work correctly.
 * <p>
 * From https://gist.github.com/patrickhammond/732d20b6e89fda23be1b
 */
public class ParcellingEnforcer
{
    public static Parcelable immediateDeepParcel(Parcelable input)
    {
        return immediateDeepParcel(input, input.getClass().getClassLoader());
    }


    public static Parcelable immediateDeepParcel(Parcelable input, ClassLoader classLoader)
    {
        Parcel parcel = null;
        try
        {
            parcel = Parcel.obtain();
            parcel.writeParcelable(input, 0);
            parcel.setDataPosition(0);
            return parcel.readParcelable(classLoader);
        }
        finally
        {
            if (parcel != null)
            {
                parcel.recycle();
            }
        }
    }
}