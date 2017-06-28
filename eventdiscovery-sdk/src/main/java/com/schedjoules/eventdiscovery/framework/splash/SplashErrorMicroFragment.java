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

package com.schedjoules.eventdiscovery.framework.splash;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.eventlist.EventListLoaderMicroFragment;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.transitions.Faded;
import org.dmfs.android.microfragments.transitions.ForwardTransition;


/**
 * Error fragment to show when the splash screen loading failed.
 *
 * @author Gabor Keszthelyi
 */
public final class SplashErrorMicroFragment implements MicroFragment<Bundle>
{
    private final Bundle mEntryArgs;


    public SplashErrorMicroFragment(Bundle entryArgs)
    {
        mEntryArgs = entryArgs;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        throw new UnsupportedOperationException("This fragment doesn't have title");
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host)
    {
        return new ErrorFragment();
    }


    @NonNull
    @Override
    public Bundle parameter()
    {
        return mEntryArgs;
    }


    @Override
    public boolean skipOnBack()
    {
        return true;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeBundle(mEntryArgs);
    }


    public static final Creator<SplashErrorMicroFragment> CREATOR = new Parcelable.Creator<SplashErrorMicroFragment>()
    {
        @Override
        public SplashErrorMicroFragment createFromParcel(Parcel in)
        {
            return new SplashErrorMicroFragment(in.readBundle(getClass().getClassLoader()));
        }


        @Override
        public SplashErrorMicroFragment[] newArray(int size)
        {
            return new SplashErrorMicroFragment[size];
        }
    };


    public static final class ErrorFragment extends BaseFragment
    {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.schedjoules_fragment_splash_error, container, false);
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentEnvironment<Bundle> environment = new FragmentEnvironment<>(ErrorFragment.this);
                    Bundle entryArgs = environment.microFragment().parameter();
                    environment.host().execute(getActivity(), new Faded(new ForwardTransition<>(new EventListLoaderMicroFragment(entryArgs))));
                }
            });
            return view;
        }

    }
}
