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

package com.schedjoules.eventdiscovery.framework.googleapis.requests;

import android.os.AsyncTask;

import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApiRequest;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApis;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.AbstractGoogleApiRequestException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiExecutionException;

import java.lang.ref.WeakReference;


/**
 * Basic {@link AsyncTask} for {@link GoogleApiRequest}s.
 *
 * @author Gabor Keszthelyi
 */
public final class GoogleApiTask<T> extends AsyncTask<GoogleApis, Void, GoogleApiTask.Result<T>>
{
    private final GoogleApiRequest<T> mGoogleApiRequest;
    private final WeakReference<Callback<T>> mCallback;


    public GoogleApiTask(GoogleApiRequest<T> googleApiRequest, Callback<T> callback)
    {
        mGoogleApiRequest = googleApiRequest;
        mCallback = new WeakReference<>(callback);
    }


    @Override
    protected Result<T> doInBackground(GoogleApis... googleApises)
    {
        try
        {
            return new SuccessResult<>(googleApises[0].execute(mGoogleApiRequest));
        }
        catch (AbstractGoogleApiRequestException e)
        {
            return new ExceptionResult<>(e);
        }
        catch (Exception e)
        {
            return new ExceptionResult<>(new GoogleApiExecutionException("Error during execution", e));
        }
    }


    @Override
    protected final void onPostExecute(Result<T> result)
    {
        Callback<T> callback = mCallback.get();
        if (callback != null)
        {
            callback.onTaskFinish(result);
        }
    }


    public interface Callback<T>
    {
        void onTaskFinish(Result<T> result);
    }


    public interface Result<T>
    {
        T value() throws AbstractGoogleApiRequestException;
    }


    private static final class SuccessResult<T> implements Result<T>
    {
        private final T mValue;


        private SuccessResult(T value)
        {
            mValue = value;
        }


        @Override
        public T value() throws AbstractGoogleApiRequestException
        {
            return mValue;
        }
    }


    private static final class ExceptionResult<T> implements Result<T>
    {
        private final AbstractGoogleApiRequestException mException;


        private ExceptionResult(AbstractGoogleApiRequestException exception)
        {
            mException = exception;
        }


        @Override
        public T value() throws AbstractGoogleApiRequestException
        {
            throw mException;
        }
    }
}
