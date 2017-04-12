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

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;


/**
 * Trimming {@link CharSequence} decorator.
 *
 * @author Gabor Keszthelyi
 */
public final class Trimmed extends AbstractCharSequence
{
    public Trimmed(final CharSequence input)
    {
        super(new Factory<CharSequence>()
        {
            @Override
            public CharSequence create()
            {
                return trim(input);
            }
        });
    }


    // Implementation 'copied' from String.trim():
    private static CharSequence trim(CharSequence input)
    {
        int start = 0;
        int last = input.length() - 1;
        int end = last;
        while ((start <= end) && (input.charAt(start) <= ' '))
        {
            start++;
        }
        while ((end >= start) && (input.charAt(end) <= ' '))
        {
            end--;
        }
        if (start == 0 && end == last)
        {
            return input;
        }
        return input.subSequence(start, end - start + 1);
    }
}
