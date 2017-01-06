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

package com.schedjoules.eventdiscovery.framework.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.eventdiscovery.R;


/**
 * A simple fragment that just informs the user of a background operation.
 *
 * @author Marten Gajda
 */
public final class LoaderFragment extends Fragment
{
    private static final int WAIT_MESSAGE_DELAY_MILLIS = 5000;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View result = inflater.inflate(R.layout.schedjoules_fragment_loader, container, false);
        // TODO: the delay and animation will be started on every rotate, which is not correct
        result.findViewById(android.R.id.message).animate().alpha(1).setStartDelay(WAIT_MESSAGE_DELAY_MILLIS).start();
        return result;
    }
}
