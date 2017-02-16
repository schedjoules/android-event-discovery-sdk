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
 * Represents a single Android permission.
 *
 * @author Gabor Keszthelyi
 */
public interface Permission<S extends PermissionStatus>
{
    /**
     * The status of this permission.
     */
    S status();

    /**
     * Initiates requesting this permission from the user.
     *
     * @param callback
     *         the callback to deliver the result
     */
    void request(PermissionRequestCallback<S> callback);
}
