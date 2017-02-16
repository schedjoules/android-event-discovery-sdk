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

package com.schedjoules.eventdiscovery.framework.permission;

/**
 * Callback to deliver the result for a permission request.
 *
 * @author Gabor Keszthelyi
 */
public interface PermissionRequestCallback<S extends PermissionStatus>
{

    /**
     * Called when the user answered the permission dialog.
     *
     * @param newStatus
     *         the new status of {@link Permission}
     */
    void onUserAnswered(S newStatus);

    /**
     * Called when the permission request has been interrupted for some reason.
     */
    void onRequestInterrupted();
}
