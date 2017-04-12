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

import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;


/**
 * {@link Spanned} decorator that trims leading and trailing empty lines.
 *
 * @author Gabor Keszthelyi
 */
public final class EmptyLinesTrimmed extends AbstractSpanned
{

    public EmptyLinesTrimmed(final Spanned input)
    {
        super(new Factory<Spanned>()
        {
            @Override
            public Spanned create()
            {
                return trimLines(new SpannableStringBuilder(input));
            }
        });
    }


    // Implementation based on String.trim():
    private static Spanned trimLines(Spanned input)
    {
        int length = input.length();
        if (length == 0)
        {
            return input;
        }

        int start = 0;
        int last = length - 1;
        int end = last;
        while ((start <= end) && (input.charAt(start) == '\n'))
        {
            start++;
        }
        while ((end >= start) && (input.charAt(end) == '\n'))
        {
            end--;
        }

        if (start == 0 && end == last)
        {
            return input;
        }
        else
        {
            SpannableStringBuilder result = new SpannableStringBuilder(input);
            if (start != 0)
            {
                result.delete(0, start);
            }
            if (end != last)
            {
                result.delete(end + 1 - start, length - start);
            }
            return result;
        }
    }
}
