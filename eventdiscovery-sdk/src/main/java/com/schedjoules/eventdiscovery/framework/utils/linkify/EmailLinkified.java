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

package com.schedjoules.eventdiscovery.framework.utils.linkify;

import android.support.v4.text.util.LinkifyCompat;
import android.text.Spannable;
import android.text.util.Linkify;

import com.schedjoules.eventdiscovery.framework.utils.spanned.AbstractSpannable;


/**
 * A {@link Spannable} that linkifies email addresses in the input {@link Spannable}.
 *
 * @author Gabor Keszthelyi
 */
public final class EmailLinkified extends AbstractSpannable
{
    public EmailLinkified(final Spannable input)
    {
        super(new com.schedjoules.eventdiscovery.framework.utils.factory.Factory<Spannable>()
        {
            @Override
            public Spannable create()
            {
                LinkifyCompat.addLinks(input, Linkify.EMAIL_ADDRESSES);
                return input;
            }
        });
    }

}
