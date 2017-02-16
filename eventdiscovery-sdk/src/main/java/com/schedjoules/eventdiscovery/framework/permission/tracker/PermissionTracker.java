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

package com.schedjoules.eventdiscovery.framework.permission.tracker;

import android.app.Activity;

import com.schedjoules.eventdiscovery.framework.permission.Permission;
import com.schedjoules.eventdiscovery.framework.permission.PermissionStatus;
import com.schedjoules.eventdiscovery.framework.permission.broadcast.PermissionRequestResult;


/**
 * Interface for a component that implements the 'tracking mechanism' for a certain {@link PermissionStatus}.
 * <p>
 * Because the state check supported by Android is limited, in order to have more detailed status, implementation might need to store values, and register
 * {@link Activity#onRequestPermissionsResult(int, String[], int[])} results with the should show rationale flag at that moment. See <a
 * href="http://stackoverflow.com/q/30719047/4247460">Related StackOverflow discussion</a>
 * <p>
 * The aim of this interface is to have those coupled logic - the checking of the status and the registering of the permission results - together. But it should
 * be used under a {@link Permission}.
 *
 * @author Gabor Keszthelyi
 */
public interface PermissionTracker<S extends PermissionStatus>
{
    /**
     * Tells the current status of a permission.
     */
    S checkStatus(Activity activity, String permissionName);

    /**
     * Creates a trackable result that can be registered, that is making sure that the appropriate states are updated and saved.
     */
    TrackablePermissionRequestResult trackableResult(PermissionRequestResult result);

}
