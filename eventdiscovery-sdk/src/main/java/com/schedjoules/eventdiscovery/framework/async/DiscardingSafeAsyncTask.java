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

package com.schedjoules.eventdiscovery.framework.async;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import java.lang.ref.WeakReference;


/**
 * Improved {@link AsyncTask} with a callback (kept with WeakReference) that delivers both success and failed results along with the input params of the task,
 * and with a {@link DiscardCheck} callback that is called from multiple points in the execution to enable the client to discard/cancel the task.
 *
 * @author Gabor Keszthelyi
 */
public abstract class DiscardingSafeAsyncTask<TASK_PARAM, EXECUTE_PARAM, PROGRESS, TASK_RESULT> extends AsyncTask<EXECUTE_PARAM, PROGRESS, SafeAsyncTaskResult<TASK_RESULT>>
{
    private final TASK_PARAM mTaskParam;
    private final WeakReference<SafeAsyncTaskCallback<TASK_PARAM, TASK_RESULT>> mCallback;
    private final WeakReference<DiscardCheck<TASK_PARAM>> mDiscardCheck;


    public DiscardingSafeAsyncTask(
            TASK_PARAM taskParam,
            SafeAsyncTaskCallback<TASK_PARAM, TASK_RESULT> callback,
            DiscardCheck<TASK_PARAM> discardCheck)
    {
        mTaskParam = taskParam;
        mCallback = new WeakReference<>(callback);
        mDiscardCheck = new WeakReference<>(discardCheck);
    }


    @Override
    protected final void onPreExecute()
    {
        if (shouldDiscard())
        {
            cancel(false);
        }
    }


    @WorkerThread
    @Override
    protected final SafeAsyncTaskResult<TASK_RESULT> doInBackground(EXECUTE_PARAM... params)
    {
        if (isCancelled())
        {
            return null;
        }
        if (shouldDiscard())
        {
            cancel(false);
            return null;
        }

        try
        {
            return new SuccessTaskResult<>(doInBackgroundWithException(mTaskParam, params));
        }
        catch (Exception e)
        {
            return new ErrorTaskResult<>(e);
        }
    }


    @WorkerThread
    protected abstract TASK_RESULT doInBackgroundWithException(TASK_PARAM taskParam, EXECUTE_PARAM... executeParams) throws Exception;


    @MainThread
    @Override
    protected final void onPostExecute(SafeAsyncTaskResult<TASK_RESULT> taskResult)
    {
        if (shouldDiscard())
        {
            return;
        }

        if (mCallback.get() != null)
        {
            mCallback.get().onTaskFinish(taskResult, mTaskParam);
        }
    }


    private boolean shouldDiscard()
    {
        return mDiscardCheck.get() != null && mDiscardCheck.get().shouldDiscard(mTaskParam);
    }
}
