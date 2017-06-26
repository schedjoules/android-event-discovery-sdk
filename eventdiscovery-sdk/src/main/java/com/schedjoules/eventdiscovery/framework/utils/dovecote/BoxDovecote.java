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

package com.schedjoules.eventdiscovery.framework.utils.dovecote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;

import org.dmfs.pigeonpost.Cage;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.Pigeon;
import org.dmfs.pigeonpost.localbroadcast.tools.MainThreadExecutor;


/**
 * A {@link Dovecote} that receives {@link Pigeon} with a {@link LocalBroadcastManager}.
 * <p>
 * The {@link Pigeon} payload can be a {@link Box} corresponding to a {@link Key}, the {@link Box}'s content will be delivered in the callback.
 * <p>
 * {@link BoxCage}s are good for communication within the same process. Note that sent {@link Pigeon}s are lost if there are no active {@link
 * BoxDovecote}s available listening for the same {@link Key}.
 *
 * @author Gabor Keszthelyi
 */
public final class BoxDovecote<T> implements Dovecote<Box<T>>
{
    private final Context mContext;
    private final Key<T> mKey;
    private final BroadcastReceiver mReceiver;


    public BoxDovecote(@NonNull Context context, @NonNull Key<T> key, @NonNull OnPigeonReturnCallback<T> callback)
    {
        mContext = context;
        mKey = key;
        mReceiver = new BoxDovecoteReceiver<>(callback, key);
        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, new IntentFilter(key.name()));
    }


    @NonNull
    @Override
    public Cage<Box<T>> cage()
    {
        return new BoxCage<T>(mKey);
    }


    @Override
    public void dispose()
    {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }


    private static final class BoxDovecoteReceiver<T> extends BroadcastReceiver
    {
        private final Dovecote.OnPigeonReturnCallback<T> mCallback;
        private final Key<T> mKey;


        public BoxDovecoteReceiver(@NonNull Dovecote.OnPigeonReturnCallback<T> callback, Key<T> key)
        {
            mCallback = callback;
            mKey = key;
        }


        @Override
        public void onReceive(Context context, final Intent intent)
        {
            MainThreadExecutor.INSTANCE.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    mCallback.onPigeonReturn(new Argument<>(mKey, intent).get());
                }
            });
        }
    }

}
