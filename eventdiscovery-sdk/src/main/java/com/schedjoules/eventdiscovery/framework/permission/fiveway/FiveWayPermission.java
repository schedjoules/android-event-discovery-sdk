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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.schedjoules.eventdiscovery.framework.permission.Permission;
import com.schedjoules.eventdiscovery.framework.permission.PermissionRequestCallback;
import com.schedjoules.eventdiscovery.framework.permission.broadcast.PermissionRequestResult;
import com.schedjoules.eventdiscovery.framework.permission.broadcast.PermissionResultBroadcast;

import static com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus.NOT_IN_MANIFEST;


/**
 * {@link Permission} supporting {@link FiveWayPermissionStatus}-es.
 *
 * @author Gabor Keszthelyi
 */
public final class FiveWayPermission implements Permission<FiveWayPermissionStatus>
{

    private static final int REQUEST_CODE = 45417;

    private final Activity mActivity;
    private final String mPermissionName;


    public FiveWayPermission(Activity activity, String permissionName)
    {
        mActivity = activity;
        mPermissionName = permissionName;
    }


    @Override
    public FiveWayPermissionStatus status()
    {
        return FiveWayPermissionTracker.getInstance().checkStatus(mActivity, mPermissionName);
    }


    @Override
    public void request(final PermissionRequestCallback<FiveWayPermissionStatus> callback)
    {
        FiveWayPermissionStatus currentStatus = status();
        if (currentStatus.granted() || currentStatus == NOT_IN_MANIFEST)
        {
            callback.onUserAnswered(currentStatus);
        }

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                // TODO The request code check and the request interrupted check would be common among permission broadcast clients, so this should probably be improved

                PermissionRequestResult result = intent.getBundleExtra("com.schedjoules.nestedExtras").getParcelable(PermissionResultBroadcast.EXTRA_RESULT);

                if (result.requestCode != REQUEST_CODE)
                {
                    return;
                }

                FiveWayPermissionTracker.getInstance().trackableResult(result).register(mActivity);

                if (result.grantResults.length != 0)
                {
                    callback.onUserAnswered(status());
                }
                else
                {
                    callback.onRequestInterrupted();
                }

                LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(this);
            }
        }, new IntentFilter(PermissionResultBroadcast.ACTION));

        ActivityCompat.requestPermissions(mActivity, new String[] { mPermissionName }, REQUEST_CODE);
    }
}
