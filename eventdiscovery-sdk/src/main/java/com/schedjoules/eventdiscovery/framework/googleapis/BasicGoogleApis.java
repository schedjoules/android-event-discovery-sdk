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

package com.schedjoules.eventdiscovery.framework.googleapis;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.AbstractGoogleApiRequestException;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.factory.PeekableLazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.ThreadSafeLazy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Basic implementation for {@link GoogleApis}.
 *
 * @author Gabor Keszthelyi
 */
public class BasicGoogleApis implements GoogleApis
{
    private static final String TAG = "BasicGoogleApis";

    private static int clientIdCounter = 1264;

    private final FragmentActivity mActivity;
    private final Set<Api> mApis;

    private PeekableLazy<GoogleApiClient> mGoogleApiClient;


    public BasicGoogleApis(FragmentActivity activity, Api... apis)
    {
        mActivity = activity;
        mApis = new HashSet<>(Arrays.asList(apis));
        mGoogleApiClient = new ThreadSafeLazy<>(new GoogleApiClientFactory());
    }


    @Override
    public <T> T execute(GoogleApiRequest<T> request) throws AbstractGoogleApiRequestException
    {
        if (!mApis.contains(request.requiredApi()))
        {
            synchronized (this)
            {
                Log.i("BasicGoogleApis", "Adding new Api: " + request.requiredApi());
                mApis.add(request.requiredApi());
                if (mGoogleApiClient.isCreated())
                {
                    GoogleApiClient googleApiClient = mGoogleApiClient.get();
                    googleApiClient.stopAutoManage(mActivity);
                    googleApiClient.disconnect();
                }
                mGoogleApiClient = new ThreadSafeLazy<>(new GoogleApiClientFactory());
            }
        }
        return request.execute(mGoogleApiClient.get());
    }


    private final class GoogleApiClientFactory implements Factory<GoogleApiClient>
    {

        @Override
        public GoogleApiClient create()
        {
            Log.d(TAG, String.format("Building GoogleApiClient with id %d and Apis: %s", clientIdCounter, mApis));
            GoogleApiClient.Builder builder = new GoogleApiClient.Builder(mActivity);
            for (Api api : mApis)
            {
                builder.addApiIfAvailable(api);
            }
            builder.enableAutoManage(mActivity, clientIdCounter++, null);
            return builder.build();
        }
    }
}
