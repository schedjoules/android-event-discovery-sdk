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
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskResult;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListDownloadTask.TaskParam;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListDownloadTask.TaskResult;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.service.ApiService;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.TOP;


/**
 * The implementation of {@link EventListController}.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListControllerImpl implements EventListController
{
    public static final int CLOSE_TO_TOP_OR_BOTTOM_THRESHOLD = 30;

    private static final String TAG = EventListControllerImpl.class.getSimpleName();

    private final FutureServiceConnection<ApiService> mApiService;

    private final ExecutorService mExecutorService;
    private final EventListItems<IFlexible, FlexibleAdapter<IFlexible>> mItems;
    private final EnumMap<ScrollDirection, ResultPage<Envelope<Event>>> mLastResultPages;
    private Map<ScrollDirection, Boolean> mIsLoading;

    private DownloadTaskClient mDownloadTaskClient;

    private Map<ScrollDirection, Boolean> mIsInErrorMode;
    private Map<ScrollDirection, TaskParam> mErrorTaskParam;


    public EventListControllerImpl(FutureServiceConnection<ApiService> apiService, EventListItems<IFlexible, FlexibleAdapter<IFlexible>> items)
    {
        mApiService = apiService;
        mItems = items;
        mExecutorService = Executors.newSingleThreadExecutor();
        mDownloadTaskClient = new DownloadTaskClient();
        mErrorTaskParam = new EnumMap<>(ScrollDirection.class);

        mIsInErrorMode = new EnumMap<>(ScrollDirection.class);
        mIsInErrorMode.put(TOP, false);
        mIsInErrorMode.put(BOTTOM, false);

        mIsLoading = new EnumMap<>(ScrollDirection.class);
        mIsLoading.put(TOP, false);
        mIsLoading.put(BOTTOM, false);

        mLastResultPages = new EnumMap<>(ScrollDirection.class);
    }


    @Override
    public void showEvents(ResultPage<Envelope<Event>> resultPage)
    {
        new ComposeListAndShowTask(resultPage).executeOnExecutor(mExecutorService);
    }


    @Override
    public void setAdapter(RecyclerView.Adapter adapter)
    {
        mItems.setAdapter((FlexibleAdapter) adapter); // TODO remove cast
    }


    private void queueDownloadTask(ApiQuery<ResultPage<Envelope<Event>>> query, ScrollDirection scrollDirection)
    {
        //noinspection unchecked
        new EventListDownloadTask(new TaskParam(query, scrollDirection), mDownloadTaskClient)
                .executeOnExecutor(mExecutorService, mApiService);
        markLoadStarted(scrollDirection);
    }


    private void queueDownloadTask(TaskParam taskParam)
    {
        //noinspection unchecked
        new EventListDownloadTask(taskParam, mDownloadTaskClient).executeOnExecutor(mExecutorService, mApiService);
        markLoadStarted(taskParam.mDirection);
    }


    private void queueComingPage(ScrollDirection direction)
    {
        if (direction.hasComingPageQuery(mLastResultPages))
        {
            queueDownloadTask(direction.comingPageQuery(mLastResultPages), direction);
        }
    }


    @Override
    public void onScrolledCloseToEdge(ScrollDirection direction)
    {
        if (!mIsLoading.get(direction))
        {
            if (mIsInErrorMode.get(direction))
            {
                queueDownloadTask(mErrorTaskParam.get(direction));
            }
            else
            {
                queueComingPage(direction);
            }
        }
    }


    private void markLoadStarted(ScrollDirection direction)
    {
        mIsLoading.put(direction, true);
        // This request will result in empty first page, so not worth showing the loading
        if (!(direction == TOP && mItems.isTodayShown()))
        {
            mItems.addSpecialItemPost(direction.loadingIndicatorItem(), direction);
        }
    }


    private void markLoadFinishedSuccess(ScrollDirection direction)
    {
        mIsLoading.put(direction, false);

        mItems.removeSpecialItem(direction.loadingIndicatorItem(), direction);

        // Resuming from error mode:
        if (mIsInErrorMode.get(direction))
        {
            mItems.removeSpecialItem(direction.errorItem(), direction);
            mIsInErrorMode.put(direction, false);
        }

        if (!direction.hasComingPageQuery(mLastResultPages)
                && (direction == BOTTOM || (direction == TOP && !mItems.isTodayShown())))
        {
            mItems.addSpecialItemNow(direction.noMoreEventsItem(), direction);
        }
    }


    private void markLoadFinishedError(ScrollDirection direction)
    {
        mIsLoading.put(direction, false);

        mItems.removeSpecialItem(direction.loadingIndicatorItem(), direction);

        if (!mIsInErrorMode.get(direction))
        {
            mItems.addSpecialItemNow(direction.errorItem(), direction);
            mIsInErrorMode.put(direction, true);
        }
    }


    private final class DownloadTaskClient implements EventListDownloadTask.Client
    {

        // Called from both background and main thread
        @Override
        public boolean shouldDiscard(TaskParam taskParam)
        {
            return mIsInErrorMode.get(taskParam.mDirection)
                    && !taskParam.equals(mErrorTaskParam.get(taskParam.mDirection));
        }


        @Override
        public void onTaskFinish(SafeAsyncTaskResult<TaskResult> result, TaskParam taskParam)
        {
            try
            {
                onTaskSuccess(result.value(), taskParam);
            }
            catch (Exception e)
            {
                onTaskError(taskParam, e);
            }
        }


        private void onTaskSuccess(TaskResult result, TaskParam taskParam)
        {
            mLastResultPages.put(taskParam.mDirection, result.mResultPage);

            mItems.mergeNewItems(result.mListItems, taskParam.mDirection);

            markLoadFinishedSuccess(taskParam.mDirection);

            if (taskParam.mQuery instanceof InitialEventsDiscovery)
            {
                mLastResultPages.put(TOP, result.mResultPage);
                queueComingPage(TOP);
            }
        }


        private void onTaskError(TaskParam taskParam, Exception e)
        {
            mErrorTaskParam.put(taskParam.mDirection, taskParam);
            Log.e(TAG, "Error during download task", e);
            markLoadFinishedError(taskParam.mDirection);
        }
    }


    private class ComposeListAndShowTask extends AsyncTask<Void, Void, List<IFlexible>>
    {
        private final ResultPage<Envelope<Event>> mResultPage;


        private ComposeListAndShowTask(ResultPage<Envelope<Event>> resultPage)
        {
            mResultPage = resultPage;
        }


        @Override
        protected List<IFlexible> doInBackground(Void... params)
        {
            return EventListItemsComposer.INSTANCE.compose(mResultPage);
        }


        @Override
        protected void onPostExecute(List<IFlexible> listItems)
        {
            mLastResultPages.put(BOTTOM, mResultPage);
            mItems.mergeNewItems(listItems, BOTTOM);
            markLoadFinishedSuccess(BOTTOM);

            mLastResultPages.put(TOP, mResultPage);
            queueComingPage(TOP);
        }
    }

}
