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

package com.schedjoules.eventdiscovery.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.eventdiscovery.R;


/**
 * Created by marten on 10.11.16.
 */
public final class FeedbackFragment extends Fragment implements View.OnClickListener
{
    public FeedbackFragment()
    {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.schedjoules_beta_feedback_bar, container, false);
        view.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v)
    {
        Intent feedbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/forms/IcGLVceoRvG2F1Fe2"));
        getActivity().startActivity(feedbackIntent);
    }
}
