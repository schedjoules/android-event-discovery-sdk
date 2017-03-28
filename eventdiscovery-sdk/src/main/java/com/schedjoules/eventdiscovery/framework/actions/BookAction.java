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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;

import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.parameters.BasicParameterType;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.types.Link;


/**
 * An {@link Action} for bookings.
 *
 * @author Marten Gajda
 */
public final class BookAction implements Action
{
    private final static ParameterType<String> TYPE = new BasicParameterType<>("http://schedjoules.com/booking/type", new PlainStringHeaderConverter());

    private final Link mLink;
    private final Event mEvent;


    public BookAction(@NonNull Link link, Event event)
    {
        mLink = link;
        mEvent = event;
    }


    @NonNull
    @Override
    public String label(@NonNull Context context)
    {
        switch (subtype())
        {
            case "hotel":
                return context.getString(R.string.schedjoules_action_book_hotel);
            case "taxi":
                return context.getString(R.string.schedjoules_action_book_taxi);
            case "parking":
                return context.getString(R.string.schedjoules_action_book_parking);
            case "ticket":
                // fall through - same as default
            default:
                return context.getString(R.string.schedjoules_action_book);
        }
    }


    @NonNull
    @Override
    public Drawable icon(@NonNull Context context)
    {
        switch (subtype())
        {
            case "hotel":
                return ContextCompat.getDrawable(context, R.drawable.schedjoules_ic_action_hotel);
            case "taxi":
                return ContextCompat.getDrawable(context, R.drawable.schedjoules_ic_action_taxi);
            case "parking":
                return ContextCompat.getDrawable(context, R.drawable.schedjoules_ic_action_parking);
            case "ticket":
                // fall through - same as default
            default:
                return ContextCompat.getDrawable(context, R.drawable.schedjoules_ic_action_book);
        }
    }


    @NonNull
    @Override
    public ActionExecutable actionExecutable()
    {
        return new ViewIntentActionExecutable(mLink, mEvent);
    }


    private String subtype()
    {
        return mLink.firstParameter(TYPE, "ticket").value();
    }

}
