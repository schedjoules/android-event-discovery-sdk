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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.schedjoules.eventdiscovery.framework.permission.broadcast.PermissionRequestResult;
import com.schedjoules.eventdiscovery.framework.permission.tracker.PermissionTracker;
import com.schedjoules.eventdiscovery.framework.permission.tracker.TrackablePermissionRequestResult;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus.DENIED;
import static com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus.DENIED_WITH_NEVER_ASK_AGAIN;
import static com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus.GRANTED;
import static com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus.NOT_ASKED_YET;
import static com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus.NOT_IN_MANIFEST;


/**
 * {@link PermissionTracker} for the {@link FiveWayPermissionStatus}s. It uses {@link SharedPreferences} to store the status, it's needed to be able to tell
 * whether user has checked the "Never ask again" checkbox previously.
 *
 * @author Gabor Keszthelyi
 */
final class FiveWayPermissionTracker implements PermissionTracker<FiveWayPermissionStatus>
{
    private static final String TAG = "PermissionStatus";
    private static final String SHARED_PREF_STORE_NAME = "SchedJoules.Permissions";
    private static final boolean LOG_ENABLED = false;

    private static FiveWayPermissionTracker sInstance;


    public static FiveWayPermissionTracker getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new FiveWayPermissionTracker();
        }
        return sInstance;
    }


    private FiveWayPermissionTracker()
    {
    }


    @Override
    public FiveWayPermissionStatus checkStatus(Activity activity, String permissionName)
    {
        return checkStatus(activity, permissionName, true, LOG_ENABLED);
    }


    @Override
    public TrackablePermissionRequestResult trackableResult(final PermissionRequestResult result)
    {
        return new TrackablePermissionRequestResult()
        {
            @Override
            public void register(Activity activity)
            {
                registerResult(activity, result.permissions, result.grantResults);
            }
        };
    }


    private FiveWayPermissionStatus checkStatus(Activity activity, String permissionName, boolean updateIfMismatch, boolean logEnabled)
    {
        SharedPreferences prefs = activity.getSharedPreferences(SHARED_PREF_STORE_NAME, Context.MODE_PRIVATE);

        int systemState = ContextCompat.checkSelfPermission(activity, permissionName);
        boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName);
        FiveWayPermissionStatus storedState = FiveWayPermissionStatus.valueOf(prefs.getString(permissionName, NOT_ASKED_YET.name()));

        FiveWayPermissionStatus newStoredState = storedState;

        if (updateIfMismatch)
        {
            if (systemState == PERMISSION_GRANTED)
            {
                // This can only be GRANTED, so we update it if there was any mismatch:
                newStoredState = GRANTED;
            }
            else // PERMISSION_DENIED:
            {
                if (shouldShowRationale)
                {
                    // This can only be DENIED, so we update it if there was any mismatch:
                    newStoredState = DENIED;
                }
                else
                {
                    if (hasPermissionInManifest(activity, permissionName))
                    {
                        // This can only by NOT_ASKED_YET or DENIED_WITH_NEVER_ASK_AGAIN, if there is a mismatch we fall back to NOT_ASKED_YET
                        newStoredState =
                                (storedState == DENIED_WITH_NEVER_ASK_AGAIN || storedState == NOT_ASKED_YET) ?
                                        storedState : NOT_ASKED_YET;

                    }
                    else
                    {
                        newStoredState = NOT_IN_MANIFEST;
                    }
                }
            }
            saveState(newStoredState, permissionName, prefs);
        }

        if (logEnabled)
        {
            logCheck(permissionName, systemState, shouldShowRationale, storedState, newStoredState, updateIfMismatch);
        }

        return newStoredState;
    }


    private void registerResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        SharedPreferences prefs = activity.getSharedPreferences(SHARED_PREF_STORE_NAME, Context.MODE_PRIVATE);

        if (permissions.length == 0)
        {
            if (LOG_ENABLED)
            {
                Log.d(TAG, "Cancelled Permission Request");
            }
            return;
        }

        for (int i = 0; i < permissions.length; i++)
        {
            boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);

            FiveWayPermissionStatus newState;
            if (grantResults[i] == PERMISSION_GRANTED)
            {
                newState = FiveWayPermissionStatus.GRANTED;
            }
            else
            {
                if (shouldShowRationale)
                {
                    newState = DENIED;
                }
                else
                {
                    if (hasPermissionInManifest(activity, permissions[i]))
                    {
                        newState = DENIED_WITH_NEVER_ASK_AGAIN;
                    }
                    else
                    {
                        newState = NOT_IN_MANIFEST;
                    }
                }
            }

            if (LOG_ENABLED)
            {
                FiveWayPermissionStatus oldState = checkStatus(activity, permissions[i], false, false);
                Log.d(TAG, String.format(
                        "PermissionRequestResult | %s | NewSystemState: %s | ShouldShowRationale: %s | OldStoredState: %s | NewStoredState: %s",
                        permissions[i], grantResults[i] == PERMISSION_GRANTED ? "Granted" : "Denied", shouldShowRationale, oldState, newState));
            }

            saveState(newState, permissions[i], prefs);
        }

    }


    private void saveState(FiveWayPermissionStatus permissionStatus, String permissionName, SharedPreferences prefs)
    {
        prefs.edit().putString(permissionName, permissionStatus.name()).apply();
    }


    private boolean hasPermissionInManifest(Activity activity, String permission)
    {
        try
        {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null)
            {
                for (String manifestPermission : info.requestedPermissions)
                {
                    if (manifestPermission.equals(permission))
                    {
                        return true;
                    }
                }
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // Can't really happen
        }
        return false;
    }


    private void logCheck(String permissionName, int systemState, boolean shouldShowRationale, FiveWayPermissionStatus storedState, FiveWayPermissionStatus newStoredState, boolean update)
    {
        if (update)
        {
            Log.d(TAG,
                    String.format(
                            "Permission Check (updating) | %s | SystemState: %s | ShouldShowRationale: %s | OldStoredState: %s | NewStoredState: %s",
                            permissionName, systemState == PERMISSION_GRANTED ? "Granted" : "Denied", shouldShowRationale, storedState, newStoredState));
        }
        else
        {
            Log.d(TAG,
                    String.format("Permission Check (non-updating) | %s | SystemState: %s | ShouldShowRationale: %s | StoredState: %s",
                            permissionName, systemState == PERMISSION_GRANTED ? "Granted" : "Denied", shouldShowRationale, storedState));
        }
    }
}
