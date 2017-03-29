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

import com.schedjoules.eventdiscovery.framework.googleapis.errors.AbstractGoogleApiRequestException;
import com.schedjoules.eventdiscovery.framework.googleapis.requests.ConnectionErrorHandling;


/**
 * Decorator for {@link GoogleApis} that applies the {@link ConnectionErrorHandling} decorator for the request, i.e. takes care of connection errors.
 *
 * @author Gabor Keszthelyi
 */
public final class Connected implements GoogleApis
{
    private final GoogleApis mDelegate;


    public Connected(GoogleApis delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public <T> T execute(GoogleApiRequest<T> request) throws AbstractGoogleApiRequestException
    {
        return mDelegate.execute(new ConnectionErrorHandling<>(request));
    }
}
