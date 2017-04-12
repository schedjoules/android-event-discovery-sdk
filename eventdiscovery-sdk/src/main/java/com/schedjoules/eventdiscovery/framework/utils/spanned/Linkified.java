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

package com.schedjoules.eventdiscovery.framework.utils.spanned;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.util.Linkify;

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;


/**
 * Linkified text.
 *
 * @author Gabor Keszthelyi
 */
public final class Linkified extends AbstractSpanned
{
    public Linkified(final CharSequence input, final int mask)
    {
        super(new Factory<Spanned>()
        {
            @Override
            public Spanned create()
            {
                SpannableString spannableString = new SpannableString(input);
                Linkify.addLinks(spannableString, mask);
                return spannableString;
            }
        });
    }


    public Linkified(final CharSequence input)
    {
        this(input, Linkify.ALL);
    }
}
