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

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Unit test for {@link Trimmed}.
 *
 * @author Gabor Keszthelyi
 */
public class TrimmedTest
{

    @Test
    public void test()
    {
        assertEquals("", new Trimmed("").toString());

        assertEquals("", new Trimmed(" ").toString());
        assertEquals("", new Trimmed("  ").toString());
        assertEquals("", new Trimmed("\n").toString());
        assertEquals("", new Trimmed("\n\r \t").toString());

        assertEquals("a", new Trimmed("a").toString());
        assertEquals("abc123", new Trimmed("abc123").toString());

        assertEquals("abc", new Trimmed("abc ").toString());
        assertEquals("abc", new Trimmed(" abc").toString());
        assertEquals("abc", new Trimmed("\nabc").toString());
        assertEquals("abc", new Trimmed("abc\n").toString());

        assertEquals("abc", new Trimmed(" abc ").toString());
        assertEquals("abc", new Trimmed("\nabc\n").toString());

        assertEquals("abc", new Trimmed("\n abc\n ").toString());

        assertEquals("ab   c", new Trimmed("\n ab   c\n ").toString());
    }

}