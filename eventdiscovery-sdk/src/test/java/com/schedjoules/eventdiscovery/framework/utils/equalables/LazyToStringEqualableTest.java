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

package com.schedjoules.eventdiscovery.framework.utils.equalables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Unit test for {@link LazyToStringEqualable}.
 *
 * @author Gabor Keszthelyi
 */
public class LazyToStringEqualableTest
{

    @Test
    public void test()
    {
        Object obj1 = new Object()
        {
            @Override
            public String toString()
            {
                return "hello";
            }
        };
        Object obj2 = new Object()
        {
            @Override
            public String toString()
            {
                return "hello";
            }
        };
        assertTrue(new LazyToStringEqualable(obj1).equals(new LazyToStringEqualable(obj2)));
        assertTrue(new LazyToStringEqualable(obj2).equals(new LazyToStringEqualable(obj1)));
        assertEquals(new LazyToStringEqualable(obj1).hashCode(), new LazyToStringEqualable(obj2).hashCode());

        Object obj3 = new Object()
        {
            @Override
            public String toString()
            {
                return "ciao";
            }
        };
        assertFalse(new LazyToStringEqualable(obj1).equals(new LazyToStringEqualable(obj3)));
    }

}