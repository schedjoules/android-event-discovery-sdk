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

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.InitialEventsDiscovery;
import com.schedjoules.eventdiscovery.framework.locationpicker.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.DateTimeBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.EventResultPageBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BundleBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.serialization.core.FluentBuilder;
import com.schedjoules.eventdiscovery.framework.utils.loadresult.BoxResult;
import com.schedjoules.eventdiscovery.framework.utils.loadresult.ErrorResult;
import com.schedjoules.eventdiscovery.framework.utils.loadresult.LoadResult;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.optional.Optional;
import org.dmfs.pigeonpost.Cage;
import org.dmfs.rfc5545.DateTime;

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

    private ApiService.FutureConnection mApiConnection;


    public static Fragment newInstance(Optional<DateTime> start, Cage<LoadResult<ResultPage<Envelope<Event>>>> resultCage)
    {
        FluentBuilder<Bundle> bundleBuilder = new BundleBuilder()
                .with(Keys.EVENTS_LOAD_RESULT_CAGE, new ParcelableBox<>(resultCage));
        if (start.isPresent())
        {
            bundleBuilder.with(Keys.DATE_TIME_START_AFTER, new DateTimeBox(start.value()));
        }
        EventListListLoaderFragment fragment = new EventListListLoaderFragment();
        fragment.setArguments(bundleBuilder.build());
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mApiConnection = new ApiService.FutureConnection(getActivity());
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

        DateTime startAfter = new OptionalArgument<>(Keys.DATE_TIME_START_AFTER, this).value(DateTime.nowAndHere());
        GeoLocation location = new SharedPrefLastSelectedPlace(getContext()).get().geoLocation();
        final InitialEventsDiscovery query = new InitialEventsDiscovery(startAfter, location);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                LoadResult<ResultPage<Envelope<Event>>> loadResult;
                try
                {
                    ResultPage<Envelope<Event>> resultPage = mApiConnection.service(5000).apiResponse(query);
                    loadResult = new BoxResult<>(new EventResultPageBox(resultPage));
                }
                catch (TimeoutException | InterruptedException | ProtocolError | IOException | ProtocolException | URISyntaxException e)
                {
                    loadResult = new ErrorResult<>();
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
        mApiConnection.disconnect();
        super.onDestroy();
    }
}
