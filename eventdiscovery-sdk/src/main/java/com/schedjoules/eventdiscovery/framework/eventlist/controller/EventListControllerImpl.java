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

import android.util.Log;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.adapter.notifier.AdapterNotifier;
import com.schedjoules.eventdiscovery.framework.async.SafeAsyncTaskResult;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListDownloadTask.TaskParam;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.EventListDownloadTask.TaskResult;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.framework.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.framework.utils.Objects;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.rfc5545.DateTime;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.TOP;


/**
 * The implementation of {@link EventListController}.
 *
 * @author Gabor Keszthelyi
 */
public class EventListControllerImpl implements EventListController, EventListBackgroundMessage.OnClickListener
{
    public static final int CLOSE_TO_TOP_OR_BOTTOM_THRESHOLD = 30;

    private static final String TAG = EventListControllerImpl.class.getSimpleName();

    private final FutureServiceConnection<ApiService> mApiService;

    private final ExecutorService mExecutorService;

    private EventListBackgroundMessage mBackgroundMessage;

    private final EventListItems mItems;
    private EventListLoadingIndicatorOverlay mLoadingIndicatorOverlay;

    private final EnumMap<ScrollDirection, ResultPage<Envelope<Event>>> mLastResultPages;

    private GeoLocation mLocation;
    private Map<ScrollDirection, Boolean> mIsLoading;

    private DownloadTaskClient mDownloadTaskClient;

    private Map<ScrollDirection, Boolean> mIsInErrorMode;
    private Map<ScrollDirection, TaskParam> mErrorTaskParam;


    public EventListControllerImpl(FutureServiceConnection<ApiService> apiService, EventListItems items)
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
    public void loadEvents(GeoLocation geoLocation, DateTime dateTime)
    {
        clearEverything();

        mLocation = geoLocation;

        queueDownloadTask(new InitialEventsDiscovery(dateTime, mLocation), BOTTOM);
    }


    @Override
    public void setAdapterNotifier(AdapterNotifier adapterNotifier)
    {
        mItems.setAdapterNotifier(adapterNotifier);
    }


    @Override
    public void setBackgroundMessageUI(EventListBackgroundMessage backgroundMessage)
    {
        mBackgroundMessage = backgroundMessage;
        mBackgroundMessage.setOnClickListener(this);
    }


    @Override
    public void setLoadingIndicatorUI(EventListLoadingIndicatorOverlay loadingIndicatorOverlay)
    {
        mLoadingIndicatorOverlay = loadingIndicatorOverlay;
    }


    private void queueDownloadTask(ApiQuery<ResultPage<Envelope<Event>>> query, ScrollDirection scrollDirection)
    {
        //noinspection unchecked
        new EventListDownloadTask(new TaskParam(mLocation, query, scrollDirection), mDownloadTaskClient)
                .executeOnExecutor(mExecutorService, mApiService);
        markLoadStarted(mItems.isEmpty(), scrollDirection);
    }


    private void queueDownloadTask(TaskParam taskParam)
    {
        //noinspection unchecked
        new EventListDownloadTask(taskParam, mDownloadTaskClient).executeOnExecutor(mExecutorService, mApiService);
        markLoadStarted(mItems.isEmpty(), taskParam.mDirection);
    }


    private void queueComingPage(ScrollDirection direction)
    {
        if (direction.hasComingPageQuery(mLastResultPages))
        {
            queueDownloadTask(direction.comingPageQuery(mLastResultPages), direction);
        }
    }


    @Override
    public void onBackgroundMessageClick()
    {
        onScrolledCloseToEdge(BOTTOM);
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


    private void clearEverything()
    {
        mItems.clear();

        mIsInErrorMode.put(TOP, false);
        mIsInErrorMode.put(BOTTOM, false);
        mIsLoading.put(TOP, false);
        mIsLoading.put(BOTTOM, false);
        mErrorTaskParam.clear();
        mLastResultPages.clear();
    }


    private void markLoadStarted(boolean isEmpty, ScrollDirection direction)
    {
        mIsLoading.put(direction, true);
        if (isEmpty)
        {
            mLoadingIndicatorOverlay.show();
        }
        else
        {
            // This request will result in empty first page, so not worth showing the loading
            if (!(direction == TOP && mItems.isTodayShown()))
            {
                mItems.addSpecialItemPost(direction.mLoadingIndicatorItem, direction);
            }
        }
    }


    private void markLoadFinishedSuccess(boolean isEmptyBefore, boolean isEmptyAfter, ScrollDirection direction)
    {
        mIsLoading.put(direction, false);

        // Hide loading:
        if (isEmptyBefore)
        {
            mLoadingIndicatorOverlay.hide();
            mBackgroundMessage.hide();
        }
        else
        {
            mItems.removeSpecialItem(direction.mLoadingIndicatorItem, direction);
        }

        // Resuming from error mode:
        if (mIsInErrorMode.get(direction))
        {
            if (isEmptyBefore)
            {
                mBackgroundMessage.hide();
            }
            else
            {
                mItems.removeSpecialItem(direction.mErrorItem, direction);
            }
            mIsInErrorMode.put(direction, false);
        }

        // No more events message:
        if (isEmptyAfter)
        {
            mBackgroundMessage.showNoEventsFoundMsg();
        }
        else if (!direction.hasComingPageQuery(mLastResultPages)
                && (direction == BOTTOM || (direction == TOP && !mItems.isTodayShown())))
        {
            mItems.addSpecialItemNow(direction.mNoMoreEventsItem, direction);
        }
    }


    private void markLoadFinishedError(boolean isEmpty, ScrollDirection direction)
    {
        mIsLoading.put(direction, false);

        // Hide loading indicator:
        if (isEmpty)
        {
            mLoadingIndicatorOverlay.hide();
        }
        else
        {
            mItems.removeSpecialItem(direction.mLoadingIndicatorItem, direction);
        }

        if (!mIsInErrorMode.get(direction))
        {
            if (isEmpty)
            {
                mBackgroundMessage.showErrorMsg();
            }
            else
            {
                mItems.addSpecialItemNow(direction.mErrorItem, direction);
            }
            mIsInErrorMode.put(direction, true);
        }
    }


    private class DownloadTaskClient implements EventListDownloadTask.Client
    {

        // Called from both background and main thread
        @Override
        public boolean shouldDiscard(TaskParam taskParam)
        {
            return !Objects.equals(taskParam.mRequestLocation, mLocation)
                    ||
                    (mIsInErrorMode.get(taskParam.mDirection)
                            && !taskParam.equals(mErrorTaskParam.get(taskParam.mDirection)));
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

            boolean isEmptyBefore = mItems.isEmpty();
            mItems.mergeNewItems(result.mListItems, taskParam.mDirection);

            markLoadFinishedSuccess(isEmptyBefore, mItems.isEmpty(), taskParam.mDirection);

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
            markLoadFinishedError(mItems.isEmpty(), taskParam.mDirection);
        }
    }
}
