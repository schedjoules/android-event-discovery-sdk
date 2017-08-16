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

import org.dmfs.httpessentials.types.Link;

import java.util.List;

import gk.android.investigator.Investigator;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * @author Gabor Keszthelyi
 */
public final class RxEventActions implements SingleSource<List<Link>>
{
    private final Context mContext;
    private final String mEventUid;


    public RxEventActions(Context context, String eventUid)
    {
        mContext = context;
        mEventUid = eventUid;
    }


    @Override
    public void subscribe(@NonNull SingleObserver<? super List<Link>> observer)
    {
        Single.wrap(new RxActionService(mContext))
                .map(new Function<ActionService, List<Link>>()
                {
                    @Override
                    public List<Link> apply(@NonNull ActionService actionService) throws Exception
                    {
                        Investigator.log(this);
                        return actionService.actions(mEventUid);
                    }
                }).subscribe(observer);
    }

}
