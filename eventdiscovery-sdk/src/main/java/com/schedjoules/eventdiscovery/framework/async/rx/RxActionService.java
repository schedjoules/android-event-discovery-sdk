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

package com.schedjoules.eventdiscovery.framework.async.rx;

import android.content.Context;

import com.schedjoules.eventdiscovery.framework.services.ActionService;

import java.util.concurrent.TimeoutException;

import gk.android.investigator.Investigator;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;


/**
 * @author Gabor Keszthelyi
 */
public class RxActionService implements SingleSource<ActionService>
{
    private final Context mContext;


    public RxActionService(Context context)
    {
        mContext = context;
    }


    @Override
    public void subscribe(@NonNull SingleObserver<? super ActionService> observer)
    {
        Single.create(new SingleOnSubscribe<ActionService>()
        {
            @Override
            public void subscribe(@NonNull SingleEmitter<ActionService> emitter) throws Exception
            {
                try
                {
                    Investigator.log(this);
                    ActionService.FutureConnection futureConnection = new ActionService.FutureConnection(mContext);
                    emitter.setDisposable(new ServiceDisposable(futureConnection));

                    ActionService service = futureConnection.service(5000);
                    emitter.onSuccess(service);
                }
                catch (TimeoutException | InterruptedException e)
                {
                    emitter.onError(e);
                }
            }
        }).subscribe(observer);
    }

}
