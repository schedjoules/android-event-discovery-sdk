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

package com.schedjoules.eventdiscovery.framework.locationpicker;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


/**
 * Represent an entry point to a Place selection UI solution.
 *
 * @author Gabor Keszthelyi
 */
public interface PlaceSelection
{

    /**
     * Prompt user to select a location.
     */
    void start(@NonNull Fragment fragment);

}
