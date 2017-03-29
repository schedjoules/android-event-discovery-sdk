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

package com.schedjoules.eventdiscovery.framework.googleapis.errors;

import com.google.android.gms.common.api.GoogleApiClient;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApiRequest;


/**
 * Exception for signalling that some error happened during execution of the {@link GoogleApiRequest}.
 * (So it's not a {@link GoogleApiClient} connection issue.)
 *
 * @author Gabor Keszthelyi
 */
public class GoogleApiExecutionException extends AbstractGoogleApiRequestException
{
    public GoogleApiExecutionException(String detailMessage)
    {
        super(detailMessage);
    }


    public GoogleApiExecutionException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

}
