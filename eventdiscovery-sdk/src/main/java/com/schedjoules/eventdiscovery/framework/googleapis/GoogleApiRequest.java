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

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.schedjoules.eventdiscovery.framework.googleapis.errors.AbstractGoogleApiRequestException;


/**
 * Request that can be executed by {@link GoogleApis}.
 *
 * @author Gabor Keszthelyi
 */
public interface GoogleApiRequest<T>
{
    /**
     * Executes this request.
     */
    T execute(GoogleApiClient googleApiClient) throws AbstractGoogleApiRequestException;

    /**
     * Returns the Google {@link Api} that is needed for this request to be executed.
     */
    Api requiredApi();

}
