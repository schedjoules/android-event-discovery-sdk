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

package com.schedjoules.eventdiscovery.framework.utils.charsequence;

import android.content.Context;


/**
 * {@link CharSequenceFactory} decorator to make the resulting {@link CharSequence} capital case.
 *
 * @author Gabor Keszthelyi
 */
public final class UpperCased implements CharSequenceFactory
{
    private final CharSequenceFactory mDelegate;


    public UpperCased(CharSequenceFactory delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public CharSequence create(Context context)
    {
        return mDelegate.create(context).toString().toUpperCase();
    }
}
