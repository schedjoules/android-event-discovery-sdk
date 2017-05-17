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

package com.schedjoules.eventdiscovery.framework.utils.dovecote;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.schedjoules.eventdiscovery.framework.serialization.commons.IntentBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.commons.StringKey;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;

import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Pigeon;


/**
 * A {@link Cage} to send {@link Box} objects corresponding to a {@link Key} via a {@link LocalBroadcastManager}.
 *
 * @author Gabor Keszthelyi
 */
public final class BoxCage<T> implements Cage<Box<T>>
{
    private final Key<T> mKey;


    public BoxCage(Key<T> key)
    {
        mKey = key;
    }


    @NonNull
    @Override
    public Pigeon<Box<T>> pigeon(@NonNull final Box<T> box)
    {
        return new Pigeon<Box<T>>()
        {
            @Override
            public void send(@NonNull Context context)
            {
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcastSync(new IntentBuilder(mKey.name()).with(mKey, box).build());
            }
        };
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mKey.name());
    }


    public static final Creator<BoxCage> CREATOR = new Creator<BoxCage>()
    {
        @Override
        public BoxCage createFromParcel(Parcel in)
        {
            return new BoxCage(new StringKey<>(in.readString()));
        }


        @Override
        public BoxCage[] newArray(int size)
        {
            return new BoxCage[size];
        }
    };
}
