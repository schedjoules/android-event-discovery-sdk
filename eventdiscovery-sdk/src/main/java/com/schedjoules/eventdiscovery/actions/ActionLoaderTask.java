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

package com.schedjoules.eventdiscovery.actions;

import android.os.AsyncTask;

import com.schedjoules.client.actions.queries.ActionsQuery;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTask;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskCallback;
import com.schedjoules.eventdiscovery.service.ApiService;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * An {@link AsyncTask} to load actions for specific events.
 *
 * @author Marten Gajda
 */
public final class ActionLoaderTask extends SafeAsyncTask<Void, Event, Void, Iterator<Iterable<Action>>>
{
    private final FutureServiceConnection<ApiService> mApiService;
    private final ActionFactory mActionFactory;


    public ActionLoaderTask(FutureServiceConnection<ApiService> apiService, ActionFactory actionFactory, SafeAsyncTaskCallback<Void, Iterator<Iterable<Action>>> resultCallback)
    {
        super(null, resultCallback);
        mApiService = apiService;
        mActionFactory = actionFactory;
    }


    @Override
    protected Iterator<Iterable<Action>> doInBackgroundWithException(Void aVoid, Event... events) throws Exception
    {
        ApiService service = mApiService.service(1000);
        // we can't return a lazy Iterator, because we need to make sure all network operations are performed in the background task
        List<Iterable<Action>> actions = new ArrayList<>(events.length);
        // TODO: use a multiget API request once available
        for (Event event : events)
        {
            actions.add(new Actions(service.apiResponse(new ActionsQuery(event.uid())), event, mActionFactory));
        }
        return actions.iterator();
    }
}
