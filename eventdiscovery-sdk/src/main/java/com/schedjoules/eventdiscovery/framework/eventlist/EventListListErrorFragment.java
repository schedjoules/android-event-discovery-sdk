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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListListErrorBinding;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.FragmentBuilder;

import org.dmfs.pigeonpost.Cage;


/**
 * Fragment to show after the event list failed to load. Contains and error message, and can be clicked to send a pigeon for retrying.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListListErrorFragment extends BaseFragment
{

    public static Fragment newInstance(Cage<Boolean> retryCage)
    {
        return new FragmentBuilder(new EventListListErrorFragment())
                .with(Keys.RETRY_EVENT_LIST_LOAD_CAGE, new ParcelableBox<>(retryCage))
                .build();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        SchedjoulesFragmentEventListListErrorBinding views =
                DataBindingUtil.inflate(inflater, R.layout.schedjoules_fragment_event_list_list_error, container, false);

        views.schedjoulesEventListBackgroundMessage.setText(R.string.schedjoules_event_list_error_bg_msg);

        views.getRoot().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Argument<>(Keys.RETRY_EVENT_LIST_LOAD_CAGE, getArguments()).get().pigeon(true).send(getContext());
            }
        });

        return views.getRoot();
    }

}
