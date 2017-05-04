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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails;

import com.schedjoules.eventdiscovery.framework.utils.charsequence.Trimmed;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.optionals.AbstractCachingOptional;
import com.schedjoules.eventdiscovery.framework.utils.spanned.Html;
import com.schedjoules.eventdiscovery.framework.utils.spanned.Linkified;

import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;


/**
 * Represents the formatted event description text.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDescriptionText extends AbstractCachingOptional<CharSequence>
{
    public EventDescriptionText(final Optional<String> description)
    {
        super(new Factory<Optional<CharSequence>>()
        {
            @Override
            public Optional<CharSequence> create()
            {
                if (!description.isPresent())
                {
                    return Absent.absent();
                }

                String desc = description.value();
                CharSequence formatted = isHtml(desc) ? new Html(desc) : new Linkified(new Trimmed(desc));
                // Checking for empty because "<p></p>" which results in empty string has been encountered
                return formatted.length() != 0 ? new Present<>(formatted) : Absent.<CharSequence>absent();
            }
        });
    }


    private static boolean isHtml(String input)
    {
        return input.contains("</") || input.contains("/>") || input.contains("<br>");
    }
}
