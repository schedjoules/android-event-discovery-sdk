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

package com.schedjoules.eventdiscovery.framework.eventlist.controller;

import android.os.AsyncTask;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.async.DiscardCheck;
import com.schedjoules.eventdiscovery.framework.async.DiscardingSafeAsyncTask;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.framework.services.EventService;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * {@link AsyncTask} for downloading and processing events for a day and location.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListDownloadTask extends
        DiscardingSafeAsyncTask<EventListDownloadTask.TaskParam, FutureServiceConnection<EventService>, Void, EventListDownloadTask.TaskResult>
{

    private EventListDownloadTask(TaskParam taskParam,
                                  SafeAsyncTaskCallback<TaskParam, TaskResult> callback,
                                  DiscardCheck<TaskParam> discardCheck)
    {
        super(taskParam, callback, discardCheck);
    }


    public EventListDownloadTask(TaskParam taskParam, EventListDownloadTask.Client client)
    {
        this(taskParam, client, client);
    }


    @Override
    protected TaskResult doInBackgroundWithException(TaskParam taskParam, FutureServiceConnection<EventService>... futureServiceConnection) throws Exception
    {
        //noinspection unchecked
        FutureServiceConnection<EventService> eventService = futureServiceConnection[0];
        ResultPage<Envelope<Event>> resultPage = eventService.service(5000).events(taskParam.mQuery);

        List<IFlexible> listItems = EventListItemsComposer.INSTANCE.compose(resultPage);

        return new TaskResult(listItems, resultPage);
    }


    public interface Client extends SafeAsyncTaskCallback<TaskParam, TaskResult>, DiscardCheck<TaskParam>
    {
    }


    public static final class TaskParam
    {

        public final ApiQuery<ResultPage<Envelope<Event>>> mQuery;
        public final ScrollDirection mDirection;


        public TaskParam(ApiQuery<ResultPage<Envelope<Event>>> query, ScrollDirection direction)
        {
            mQuery = query;
            mDirection = direction;
        }

    }


    public static final class TaskResult
    {

        public final List<IFlexible> mListItems;
        public final ResultPage<Envelope<Event>> mResultPage;


        public TaskResult(List<IFlexible> listItems, ResultPage<Envelope<Event>> resultPage)
        {
            mListItems = listItems;
            mResultPage = resultPage;
        }

    }
}
