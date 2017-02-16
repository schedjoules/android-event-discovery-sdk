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

import android.support.annotation.WorkerThread;

import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.cache.Cache;


/**
 * {@link SafeAsyncTask} that uses a {@link Cache} to get results from if they are cached, otherwise executes that task and saves the result in the cache.
 *
 * @author Gabor Keszthelyi
 */
public abstract class CachingSafeAsyncTask<TASK_PARAM extends Equalable, EXECUTE_PARAM, PROGRESS, TASK_RESULT> extends SafeAsyncTask<TASK_PARAM, EXECUTE_PARAM, PROGRESS, TASK_RESULT>
{
    private final Cache<TASK_PARAM, TASK_RESULT> mCache;


    public CachingSafeAsyncTask(TASK_PARAM taskParam, SafeAsyncTaskCallback<TASK_PARAM, TASK_RESULT> callback, Cache<TASK_PARAM, TASK_RESULT> cache)
    {
        super(taskParam, callback);
        mCache = cache;
    }


    @Override
    protected void onPreExecuteWithParam(TASK_PARAM taskParam)
    {
        TASK_RESULT cachedValue = mCache.get(taskParam);
        if (cachedValue != null)
        {
            cancel(false);
            onPostExecute(new SuccessTaskResult<>(cachedValue));
        }
    }


    @Override
    protected final TASK_RESULT doInBackgroundWithException(TASK_PARAM taskParam, EXECUTE_PARAM... execute_params) throws Exception
    {
        TASK_RESULT cachedValue = mCache.get(taskParam);
        if (cachedValue != null)
        {
            return cachedValue;
        }
        TASK_RESULT taskResult = doInBackgroundWithExceptionForNonCached(taskParam, execute_params[0]);
        mCache.put(taskParam, taskResult);
        return taskResult;
    }


    @WorkerThread
    protected abstract TASK_RESULT doInBackgroundWithExceptionForNonCached(TASK_PARAM taskParam, EXECUTE_PARAM execute_param) throws Exception;
}
