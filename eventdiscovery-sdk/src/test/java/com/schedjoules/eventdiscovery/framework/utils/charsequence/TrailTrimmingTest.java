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
 * Unit test for {@link TrailTrimming}.
 *
 * @author Gabor Keszthelyi
 */
public class TrailTrimmingTest
{

    @Test
    public void testWithVariousValues()
    {
        assertEquals("abc", new TrailTrimming("abc").toString());
        assertEquals("", new TrailTrimming("").toString());

        assertEquals("", new TrailTrimming(" ").toString());
        assertEquals("", new TrailTrimming("  ").toString());
        assertEquals("", new TrailTrimming("   ").toString());
        assertEquals("", new TrailTrimming(" \n  ").toString());
        assertEquals("", new TrailTrimming("\n").toString());
        assertEquals("", new TrailTrimming("\n\n").toString());
        assertEquals("", new TrailTrimming(" \n").toString());
        assertEquals("", new TrailTrimming("\n ").toString());

        assertEquals(" abc", new TrailTrimming(" abc").toString());
        assertEquals("abc", new TrailTrimming("abc ").toString());
        assertEquals("  abc", new TrailTrimming("  abc").toString());
        assertEquals("abc", new TrailTrimming("abc  ").toString());
        assertEquals(" abc", new TrailTrimming(" abc ").toString());
        assertEquals("  abc", new TrailTrimming("  abc  ").toString());
        assertEquals("a  bc", new TrailTrimming("a  bc").toString());

        assertEquals("abc", new TrailTrimming("abc\n").toString());
        assertEquals("\nabc", new TrailTrimming("\nabc").toString());
        assertEquals("abc", new TrailTrimming("abc\n\n\n").toString());
        assertEquals("\n\nabc", new TrailTrimming("\n\nabc").toString());
        assertEquals("\nabc", new TrailTrimming("\nabc\n\n").toString());
    }

}