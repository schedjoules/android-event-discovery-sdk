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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ApiQueryBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.EventResultPageBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.FragmentBuilder;
import com.schedjoules.eventdiscovery.framework.services.EventService;
import com.schedjoules.eventdiscovery.framework.utils.loadresult.BoxResult;
import com.schedjoules.eventdiscovery.framework.utils.loadresult.ErrorResult;
import com.schedjoules.eventdiscovery.framework.utils.loadresult.LoadResult;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.pigeonpost.Cage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;


/**
 * Fragment for loading the first event result page while showing a loading indicator.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListListLoaderFragment extends BaseFragment
{

    private EventService.FutureConnection mEventService;


    public static Fragment newInstance(ApiQuery<ResultPage<Envelope<Event>>> query, Cage<LoadResult<ResultPage<Envelope<Event>>>> resultCage)
    {
        return new FragmentBuilder(new EventListListLoaderFragment())
                .with(Keys.DISCOVERY_QUERY, new ApiQueryBox<>(query))
                .with(Keys.EVENTS_LOAD_RESULT_CAGE, new ParcelableBox<>(resultCage))
                .build();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mEventService = new EventService.FutureConnection(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.schedjoules_fragment_event_list_list_loader, container, false);
    }


    @Override
    public void onResume()
    {
        super.onResume();

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ApiQuery<ResultPage<Envelope<Event>>> query = new Argument<>(Keys.DISCOVERY_QUERY, EventListListLoaderFragment.this).get();

                LoadResult<ResultPage<Envelope<Event>>> loadResult;
                try
                {
                    ResultPage<Envelope<Event>> resultPage = mEventService.service(5000).events(query);
                    loadResult = new BoxResult<>(new EventResultPageBox(resultPage));
                }
                catch (TimeoutException | InterruptedException | ProtocolError | IOException | ProtocolException | URISyntaxException e)
                {
                    loadResult = new ErrorResult<>(e);
                }

                if (isResumed())
                {
                    new Argument<>(Keys.EVENTS_LOAD_RESULT_CAGE, getArguments()).get().pigeon(loadResult).send(getActivity());
                }
            }
        }).start();
    }


    @Override
    public void onDestroy()
    {
        mEventService.disconnect();
        super.onDestroy();
    }
}
