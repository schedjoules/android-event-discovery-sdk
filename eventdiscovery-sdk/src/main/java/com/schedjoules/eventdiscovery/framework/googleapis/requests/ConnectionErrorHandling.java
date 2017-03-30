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

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApiRequest;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.AbstractGoogleApiRequestException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.BasicGoogleApiErrorInterpreter;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiAutoManagedIssueException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiNonRecoverableException;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.GoogleApiRecoverableException;

import java.util.concurrent.TimeUnit;


/**
 * Decorator for {@link GoogleApiRequest} that takes care of the connection errors.
 *
 * @author Gabor Keszthelyi
 */
public final class ConnectionErrorHandling<T> implements GoogleApiRequest<T>
{
    private static final String TAG = "ConnectionErrorHandling";

    private final GoogleApiRequest<T> mDelegate;


    public ConnectionErrorHandling(GoogleApiRequest<T> delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public final T execute(GoogleApiClient googleApiClient) throws AbstractGoogleApiRequestException
    {
        ConnectionResult connectionResult = googleApiClient.blockingConnect(2, TimeUnit.SECONDS);
        if (!connectionResult.isSuccess())
        {
            Log.e(TAG, "Error ConnectionResult: " + connectionResult);
            if (connectionResult.hasResolution())
            {
                // This is being auto-managed
                throw new GoogleApiAutoManagedIssueException(connectionResult.toString());
            }
            else
            {
                if (new BasicGoogleApiErrorInterpreter().isRetriable(connectionResult))
                {
                    throw new GoogleApiRecoverableException(connectionResult.toString());
                }
                else
                {
                    throw new GoogleApiNonRecoverableException(connectionResult.toString());
                }
            }
        }
        return mDelegate.execute(googleApiClient);
    }


    @Override
    public Api requiredApi()
    {
        return mDelegate.requiredApi();
    }

}
