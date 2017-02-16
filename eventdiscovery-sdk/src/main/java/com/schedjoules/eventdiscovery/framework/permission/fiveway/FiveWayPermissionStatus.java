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

package com.schedjoules.eventdiscovery.framework.permission.fiveway;

import com.schedjoules.eventdiscovery.framework.permission.PermissionStatus;


/**
 * {@link PermissionStatus} that distinguishes between 4 statuses, see their meanings below.
 *
 * @author Gabor Keszthelyi
 */
public enum FiveWayPermissionStatus implements PermissionStatus
{

    /**
     * Can mean multiple things:
     * <p>
     * - Main case: User hasn't been asked about the permission
     * <p>
     * - User had been asked and denied it with "Never ask again" checked but the request wasn't done with {@link FiveWayPermission}, so the result was not
     * registered. (eg. outside of the SDK but within the app)
     * <p>
     * - User had been asked and denied it with "Never ask again" but user cleared the app data later (so SharedPref was cleared)
     */
    NOT_ASKED_YET(false),

    /**
     * The permission has been granted by the user.
     */
    GRANTED(true),

    /**
     * Can mean two things:
     * <p>
     * - Main case: The user has denied the permission but didn't checked the "Never ask again" checkbox.
     * <p>
     * - User has denied a previously granted permission from phone settings
     */
    DENIED(false),

    /**
     * Can mean two things:
     * <p>
     * - Main case: The user has denied the permission and checked the "Never ask again" checkbox.
     * <p>
     * - The permission is not in the manifest
     */
    DENIED_WITH_NEVER_ASK_AGAIN(false),

    /**
     * The permission is not declared in the manifest.
     */
    NOT_IN_MANIFEST(false);

    private final boolean mGranted;


    FiveWayPermissionStatus(boolean granted)
    {
        mGranted = granted;
    }


    @Override
    public boolean granted()
    {
        return mGranted;
    }
}
