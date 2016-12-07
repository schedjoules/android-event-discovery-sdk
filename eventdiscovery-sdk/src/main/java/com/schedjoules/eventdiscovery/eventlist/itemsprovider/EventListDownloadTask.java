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

package com.schedjoules.eventdiscovery.eventlist.itemsprovider;

import android.os.AsyncTask;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.adapter.ListItem;
import com.schedjoules.eventdiscovery.framework.async.DiscardCheck;
import com.schedjoules.eventdiscovery.framework.async.DiscardingSafeAsyncTask;
import com.schedjoules.eventdiscovery.framework.async.PreExecuteCallback;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.service.ApiService;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import java.util.List;


/**
 * {@link AsyncTask} for downloading and processing events for a day and location.
 *
 * @author Gabor Keszthelyi
 */
// TODO What EventListItemsComposer does would be nicer with a separate task, but that composition of async adds complexity as well
public final class EventListDownloadTask extends
        DiscardingSafeAsyncTask<EventListDownloadTask.TaskParam, FutureServiceConnection<ApiService>, Void, EventListDownloadTask.TaskResult>
{

    private EventListDownloadTask(
            TaskParam taskParam,
            SafeAsyncTaskCallback<TaskParam, TaskResult> callback,
            DiscardCheck<TaskParam> discardCheck,
            PreExecuteCallback<TaskParam> preExecuteCallback)
    {
        super(taskParam, callback, discardCheck, preExecuteCallback);
    }


    public EventListDownloadTask(TaskParam taskParam, EventListDownloadTask.Client client)
    {
        this(taskParam, client, client, client);
    }


    @Override
    protected TaskResult doInBackgroundWithException(TaskParam taskParam, FutureServiceConnection<ApiService>... futureServiceConnection) throws Exception
    {
        //noinspection unchecked
        FutureServiceConnection<ApiService> apiService = futureServiceConnection[0];
        ResultPage<Envelope<Event>> resultPage = apiService.service(1000).apiResponse(taskParam.mQuery);

        List<ListItem> listItems = EventListItemsComposer.INSTANCE.compose(resultPage);

        return new TaskResult(listItems, resultPage);
    }


    public static final class TaskParam
    {

        public final ApiQuery<ResultPage<Envelope<Event>>> mQuery;
        public final GeoLocation mRequestLocation;
        public final ScrollDirection mDirection;


        public TaskParam(GeoLocation requestLocation, ApiQuery<ResultPage<Envelope<Event>>> query, ScrollDirection direction)
        {
            mQuery = query;
            mRequestLocation = requestLocation;
            mDirection = direction;
        }

    }


    public static class TaskResult
    {

        public final List<ListItem> mListItems;
        public final ResultPage<Envelope<Event>> mResultPage;


        public TaskResult(List<ListItem> listItems, ResultPage<Envelope<Event>> resultPage)
        {
            mListItems = listItems;
            mResultPage = resultPage;
        }

    }


    public interface Client extends
            SafeAsyncTaskCallback<TaskParam, TaskResult>, DiscardCheck<TaskParam>, PreExecuteCallback<TaskParam>
    {
    }
}
