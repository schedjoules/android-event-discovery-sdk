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

package com.schedjoules.eventdiscovery.framework.model.recent;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;

import static junit.framework.Assert.assertEquals;


/**
 * @author Marten Gajda
 */
@RunWith(AndroidJUnit4.class)
public class SerializableCharSequenceConverterTest
{
    @Test
    public void testStringSerializable() throws Exception
    {
        CharSequenceConverter<String> charSequenceConverter = new SerializableCharSequenceConverter<>();
        assertEquals("TestString", charSequenceConverter.fromCharSequence(charSequenceConverter.fromValue("TestString")));
    }


    @Test
    public void testCustomSerializable() throws Exception
    {
        CharSequenceConverter<CustomSerializable> charSequenceConverter = new SerializableCharSequenceConverter<>();
        assertEquals(new CustomSerializable(123, "testing"),
                charSequenceConverter.fromCharSequence(charSequenceConverter.fromValue(new CustomSerializable(123, "testing"))));
    }


    private final static class CustomSerializable implements Serializable
    {
        private final int mInt;
        private final String mString;


        private CustomSerializable(int i, String string)
        {
            mInt = i;
            mString = string;
        }


        @Override
        public boolean equals(Object o)
        {
            return o instanceof CustomSerializable && mInt == ((CustomSerializable) o).mInt && mString.equals(((CustomSerializable) o).mString);
        }
    }
}