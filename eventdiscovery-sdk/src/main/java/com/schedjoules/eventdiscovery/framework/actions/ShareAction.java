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

package com.schedjoules.eventdiscovery.framework.actions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;

import org.dmfs.httpessentials.types.Link;


/**
 * {@link Action} for sharing events.
 *
 * @author Gabor Keszthelyi
 */
public final class ShareAction implements Action
{

    private final Link mLink;
    private final Event mEvent;


    public ShareAction(@NonNull Link link, @NonNull Event event)
    {
        mLink = link;
        mEvent = event;
    }


    @NonNull
    @Override
    public String shortLabel(@NonNull Context context)
    {
        return context.getString(R.string.schedjoules_action_share);
    }


    @NonNull
    @Override
    public String longLabel(@NonNull Context context)
    {
        return context.getString(R.string.schedjoules_action_share);
    }


    @NonNull
    @Override
    public Drawable icon(@NonNull Context context)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            return context.getResources().getDrawable(R.drawable.schedjoules_ic_action_share);
        }
        return context.getDrawable(R.drawable.schedjoules_ic_action_share);
    }


    @NonNull
    @Override
    public ActionExecutable actionExecutable()
    {
        return new ShareActionExecutable(mLink, mEvent);
    }
}
