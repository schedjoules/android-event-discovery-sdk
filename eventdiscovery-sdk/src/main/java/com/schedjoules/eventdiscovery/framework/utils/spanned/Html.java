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

import android.text.Spanned;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;


/**
 * Represents a Html 'formatted' text to be displayed with a {@link TextView}.
 *
 * @author Gabor Keszthelyi
 */
public final class Html extends AbstractSpanned
{
    public Html(final String inputSource)
    {
        super(new Factory<Spanned>()
        {
            @Override
            public Spanned create()
            {
                return new EmptyLinesTrimmed(android.text.Html.fromHtml(inputSource));
            }
        });
    }

}
