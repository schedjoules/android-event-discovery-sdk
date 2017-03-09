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

import android.app.Activity;


/**
 * Represents a single Android permission.
 *
 * @author Marten Gajda
 */
public interface Permission
{
    /**
     * The name of this permission.
     *
     * @return A String containing the permission name.
     */
    String name();

    /**
     * Return whether this permission is granted or not.
     *
     * @return {@code true} if this permission has been granted to the app, {@code false} otherwise.
     */
    boolean isGranted();

    /**
     * Returns whether this permission can be granted at runtime by calling {@link #request()} and sending the returned {@link PermissionRequest}.
     * On Android SDK levels <22 this will always return {@code false}.
     *
     * @param activity
     *         The current {@link Activity}.
     *
     * @return {@code true} if this permission can be granted, {@code false} otherwise.
     */
    boolean isGrantable(Activity activity);

    /**
     * Creates a {@link PermissionRequest} to request this {@link Permission}.
     *
     * @return A new {@link PermissionRequest} for this single {@link Permission}.
     */
    PermissionRequest request();
}
