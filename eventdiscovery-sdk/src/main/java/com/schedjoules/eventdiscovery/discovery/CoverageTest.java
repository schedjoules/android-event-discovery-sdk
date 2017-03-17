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

package com.schedjoules.eventdiscovery.discovery;

import android.content.Context;


/**
 * Test the API coverage of the current country, based on GEO location, network operator country, SIM card country and/or region setting, whatever is available.
 *
 * @author Marten Gajda
 */
public interface CoverageTest
{
    /**
     * Execute the {@link CoverageTest}. The result is delivered asynchronously.
     *
     * @param context
     *         The current {@link Context}.
     */
    void execute(Context context);
}
