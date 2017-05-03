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
import android.util.Patterns;

import com.schedjoules.eventdiscovery.framework.utils.spanned.AbstractSpannable;

import java.util.regex.Matcher;


/**
 * A {@link Spannable} that linkifies web urls that starts with "http" or "Http" in the input {@link Spannable}.
 *
 * @author Gabor Keszthelyi
 */
public final class CompleteWebUrlLinkifying extends AbstractSpannable
{

    private static final Linkify.MatchFilter MATCH_FILTER = new HttpMatchFilter();
    private static final Linkify.TransformFilter TRANSFORM_FILTER = new ToLowerCaseHttp();


    public CompleteWebUrlLinkifying(final Spannable input)
    {
        super(new com.schedjoules.eventdiscovery.framework.utils.factory.Factory<Spannable>()
        {
            @Override
            public Spannable create()
            {
                LinkifyCompat.addLinks(input, Patterns.WEB_URL, null, MATCH_FILTER, TRANSFORM_FILTER);
                return input;
            }
        });
    }


    /**
     * A {@link Linkify.MatchFilter} that only accepts matches that start with "http" (ignoring case)
     * (plus checks the default filter as well)
     */
    private static final class HttpMatchFilter implements Linkify.MatchFilter
    {
        @Override
        public boolean acceptMatch(CharSequence s, int start, int end)
        {
            return s.subSequence(start, end).toString().toLowerCase().startsWith("http")
                    && Linkify.sUrlMatchFilter.acceptMatch(s, start, end);
        }
    }


    /**
     * There may be a bug in Android that "Http" links don't seem to fire the intent, so this
     * {@link Linkify.TransformFilter} replaces "Http" with "http" to fix that.
     */
    private static final class ToLowerCaseHttp implements Linkify.TransformFilter
    {
        @Override
        public String transformUrl(Matcher match, String url)
        {
            return match.group().replace("Http", "http");
        }
    }
}
