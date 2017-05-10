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

import android.text.Spanned;

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.spanned.AbstractSpanned;
import com.schedjoules.eventdiscovery.framework.utils.spanned.Html;
import com.schedjoules.eventdiscovery.framework.utils.spanned.Linkified;


/**
 * {@link Spanned} that 'styles' the input string, i.e. 'format' html input or linkify plain text.
 *
 * @author Gabor Keszthelyi
 */
public final class Styled extends AbstractSpanned
{
    public Styled(final String input)
    {
        super(new Factory<Spanned>()
        {
            @Override
            public Spanned create()
            {
                return isHtml(input) ? new Html(input) : new Linkified(new Trimmed(input));
            }
        });
    }


    private static boolean isHtml(String input)
    {
        return input.contains("</") || input.contains("/>") || input.contains("<br>");
    }
}
