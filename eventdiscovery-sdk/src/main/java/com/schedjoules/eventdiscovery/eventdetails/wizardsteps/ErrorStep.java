/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.eventdetails.wizardsteps;

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

import org.dmfs.android.dumbledore.WizardStep;


/**
 * A {@link WizardStep} to present an error to the user.
 *
 * @author Marten Gajda
 */
public final class ErrorStep implements WizardStep
{
    private final String mTitle;
    private final String mErrorMessage;


    public ErrorStep()
    {
        this(null, null);
    }


    public ErrorStep(String errorMessage)
    {
        this(null, errorMessage);
    }


    public ErrorStep(String title, String errorMessage)
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
    public Fragment fragment(@NonNull Context context)
    {
        Fragment result = new ErrorFragment();
        Bundle args = new Bundle(1);
        args.putParcelable(WizardStep.ARG_WIZARD_STEP, this);
        args.putString("title", mTitle);
        args.putString("message", mErrorMessage);
        result.setArguments(args);
        return result;
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


    public final static Creator<ErrorStep> CREATOR = new Creator<ErrorStep>()
    {
        @Override
        public ErrorStep createFromParcel(Parcel source)
        {
            return new ErrorStep(source.readString(), source.readString());
        }


        @Override
        public ErrorStep[] newArray(int size)
        {
            return new ErrorStep[size];
        }
    };


    public final static class ErrorFragment extends Fragment
    {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.schedjoules_fragment_event_details_error, container, false);
            Bundle args = getArguments();
            String title = args.getString("title");
            String message = args.getString("message");
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
