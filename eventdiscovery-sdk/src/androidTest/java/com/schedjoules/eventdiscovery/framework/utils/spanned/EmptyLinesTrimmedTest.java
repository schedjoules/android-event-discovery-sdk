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

import android.support.test.runner.AndroidJUnit4;
import android.text.SpannableString;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


/**
 * Unit test for {@link EmptyLinesTrimmed}.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(AndroidJUnit4.class)
public class EmptyLinesTrimmedTest
{
    @Test
    public void test()
    {
        assertEquals("", new EmptyLinesTrimmed(new SpannableString("")).toString());
        assertEquals("hello", new EmptyLinesTrimmed(new SpannableString("hello")).toString());

        assertEquals("hello", new EmptyLinesTrimmed(new SpannableString("\nhello")).toString());
        assertEquals("hello", new EmptyLinesTrimmed(new SpannableString("hello\n")).toString());
        assertEquals("hello", new EmptyLinesTrimmed(new SpannableString("\nhello\n")).toString());
        assertEquals("hello", new EmptyLinesTrimmed(new SpannableString("\n\nhello\n\n")).toString());
        assertEquals("hello", new EmptyLinesTrimmed(new SpannableString("\n\n\n\n\nhello\n\n\n\n")).toString());

        assertEquals("", new EmptyLinesTrimmed(new SpannableString("\n")).toString());
        assertEquals("", new EmptyLinesTrimmed(new SpannableString("\n\n")).toString());
        assertEquals("", new EmptyLinesTrimmed(new SpannableString("\n\n\n")).toString());
        assertEquals("h", new EmptyLinesTrimmed(new SpannableString("h")).toString());
        assertEquals(" ", new EmptyLinesTrimmed(new SpannableString(" ")).toString());

        assertEquals(" hello", new EmptyLinesTrimmed(new SpannableString("\n hello")).toString());

        // Empty lines after/before whitespace content are not removed by EmptyLinesTrimmed:
        assertEquals("hello\n ", new EmptyLinesTrimmed(new SpannableString("hello\n ")).toString());
        assertEquals(" \nhello", new EmptyLinesTrimmed(new SpannableString(" \nhello")).toString());
    }

}