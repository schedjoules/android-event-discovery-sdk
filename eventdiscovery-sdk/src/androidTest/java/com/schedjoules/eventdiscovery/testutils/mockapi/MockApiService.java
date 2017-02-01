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

package com.schedjoules.eventdiscovery.testutils.mockapi;

import android.content.Context;
import android.support.annotation.NonNull;

import com.schedjoules.client.Api;
import com.schedjoules.eventdiscovery.service.AbstractApiService;


/**
 * {@link AbstractApiService} to be used in UI tests of the SDK.
 *
 * @author Gabor Keszthelyi
 */
public final class MockApiService extends AbstractApiService
{
    public MockApiService()
    {
        super(new ApiFactory()
        {
            @NonNull
            @Override
            public Api schedJoulesApi(@NonNull Context context)
            {
                return new MockApi();
            }
        });
    }
}
