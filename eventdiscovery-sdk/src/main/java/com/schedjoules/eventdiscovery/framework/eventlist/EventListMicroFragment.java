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

package com.schedjoules.eventdiscovery.framework.eventlist;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;


/**
 * {@link MicroFragment} for the event list screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListMicroFragment implements MicroFragment<Bundle>
{
    private final Bundle mArgs;

    // TODO Instead of the Bundle, a typed input should probably be the parameter
    // TODO Create OptionalBox<T>, so Optional<DateTime> could be used here for example


    public EventListMicroFragment(Bundle args)
    {
        mArgs = args;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        throw new UnsupportedOperationException("Title is not used for this fragment.");
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost microFragmentHost)
    {
        return new EventListFragment();
    }


    @NonNull
    @Override
    public Bundle parameter()
    {
        return mArgs;
    }


    @Override
    public boolean skipOnBack()
    {
        return false;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeBundle(mArgs);
    }


    public static final Creator<EventListMicroFragment> CREATOR = new Creator<EventListMicroFragment>()
    {
        @Override
        public EventListMicroFragment createFromParcel(Parcel in)
        {
            return new EventListMicroFragment(in.readBundle(getClass().getClassLoader()));
        }


        @Override
        public EventListMicroFragment[] newArray(int size)
        {
            return new EventListMicroFragment[size];
        }
    };
}
