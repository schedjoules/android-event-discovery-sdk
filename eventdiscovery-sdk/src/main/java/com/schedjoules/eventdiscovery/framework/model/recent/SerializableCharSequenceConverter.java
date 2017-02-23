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

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * A converter to convert {@link Serializable}s from/to {@link CharSequence}s.
 *
 * @author Marten Gajda
 */
public final class SerializableCharSequenceConverter<T extends Serializable> implements CharSequenceConverter<T>
{
    @Override
    public CharSequence fromValue(T value)
    {
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(value);
            objectOut.close();
            return Base64.encodeToString(out.toByteArray(), Base64.URL_SAFE);
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException while operating on ByteArrayOutputStream", e);
        }
    }


    @Override
    public T fromCharSequence(CharSequence chars)
    {
        try
        {
            return (T) new ObjectInputStream(new ByteArrayInputStream(Base64.decode(chars.toString(), Base64.URL_SAFE))).readObject();
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Could not find serialized class", e);
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException while operating on ByteArrayOutputStream", e);
        }
    }
}
