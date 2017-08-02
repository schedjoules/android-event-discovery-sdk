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

package com.schedjoules.eventdiscovery.framework.common;

import android.content.Context;
import android.os.Bundle;

import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;


/**
 * Lazy access for the context state bundle of a Context.
 *
 * @author Gabor Keszthelyi
 */
public final class ContextState implements Lazy<Bundle>
{
    public static final String SERVICE_CONTEXT_STATE = "schedjoules.contextState";

    private final Context mContext;


    public ContextState(Context context)
    {

        mContext = context;
    }


    @Override
    public Bundle get()
    {
        //noinspection WrongConstant
        Object contextState = mContext.getSystemService(SERVICE_CONTEXT_STATE);
        if (contextState == null)
        {
            throw new RuntimeException("Context doesn't provide context state Bundle in getSystemService()");
        }
        try
        {
            return (Bundle) contextState;
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException("Context state cannot be cast to Bundle", e);
        }
    }
}
