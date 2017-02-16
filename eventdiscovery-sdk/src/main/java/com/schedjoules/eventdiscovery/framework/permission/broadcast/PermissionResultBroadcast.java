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

package com.schedjoules.eventdiscovery.framework.permission.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.schedjoules.eventdiscovery.framework.utils.Broadcast;


/**
 * {@link Broadcast} for sending the result of a permission request.
 *
 * @author Gabor Keszthelyi
 */
public final class PermissionResultBroadcast implements Broadcast
{
    public static final String ACTION = "schedjoules.action.permission.result";
    public static final String EXTRA_RESULT = "schedjoules.extra.permission.result";

    private final PermissionRequestResult mResult;


    public PermissionResultBroadcast(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        mResult = new PermissionRequestResult(requestCode, permissions, grantResults);
    }


    @Override
    public void send(Context context)
    {
        Intent intent = new Intent(ACTION);

        Bundle nestedBundle = new Bundle(1);
        nestedBundle.putParcelable(EXTRA_RESULT, mResult);
        intent.putExtra("com.schedjoules.nestedExtras", nestedBundle);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
