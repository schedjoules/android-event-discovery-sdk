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

package com.schedjoules.eventdiscovery.microfragments.eventdetails;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.dmfs.android.microfragments.BasicMicroFragmentEnvironment;
import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;


/**
 * A {@link MicroFragment} to present an error to the user.
 *
 * @author Marten Gajda
 */
public final class ErrorMicroFragment implements MicroFragment<ErrorMicroFragment.Error>
{
    interface Error
    {
        String title();

        String message();
    }


    private final String mTitle;
    private final String mErrorMessage;


    public ErrorMicroFragment()
    {
        this(null, null);
    }


    public ErrorMicroFragment(String errorMessage)
    {
        this(null, errorMessage);
    }


    public ErrorMicroFragment(String title, String errorMessage)
    {
        mTitle = title;
        mErrorMessage = errorMessage;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        return "Error";
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost microFragmentHost)
    {
        Fragment result = new ErrorFragment();
        Bundle args = new Bundle(1);
        args.putParcelable(MicroFragment.ARG_ENVIRONMENT, new BasicMicroFragmentEnvironment<>(this, microFragmentHost));
        result.setArguments(args);
        return result;
    }


    @NonNull
    @Override
    public Error parameters()
    {
        return new Error()
        {
            @Override
            public String title()
            {
                return mTitle;
            }


            @Override
            public String message()
            {
                return mErrorMessage;
            }
        };
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
        dest.writeString(mTitle);
        dest.writeString(mErrorMessage);
    }


    public final static Creator<ErrorMicroFragment> CREATOR = new Creator<ErrorMicroFragment>()
    {
        @Override
        public ErrorMicroFragment createFromParcel(Parcel source)
        {
            return new ErrorMicroFragment(source.readString(), source.readString());
        }


        @Override
        public ErrorMicroFragment[] newArray(int size)
        {
            return new ErrorMicroFragment[size];
        }
    };


    public final static class ErrorFragment extends Fragment
    {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.schedjoules_fragment_error, container, false);
            Error error = new FragmentEnvironment<Error>(this).microFragment().parameters();
            String title = error.title();
            String message = error.message();
            if (message != null)
            {
                ((TextView) view.findViewById(android.R.id.message)).setText(message);
            }
            if (title != null)
            {
                ((CollapsingToolbarLayout) view.findViewById(R.id.schedjoules_event_detail_toolbar_layout)).setTitle(title);
            }
            else
            {
                ((CollapsingToolbarLayout) view.findViewById(R.id.schedjoules_event_detail_toolbar_layout)).setTitle(
                        getActivity().getString(R.string.schedjoules_title_error));
            }
            return view;
        }
    }
}
