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

package com.schedjoules.eventdiscovery.framework.serialization.commons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;

import org.dmfs.optional.NullSafe;
import org.dmfs.optional.Optional;

import java.util.NoSuchElementException;


/**
 * Represents an optionally present argument in a parameterizable container (Bundle or Intent practically).
 *
 * @author Gabor Keszthelyi
 */
public final class OptionalArgument<T> implements Optional<T>
{
    private final Key<T> mKey;
    private final Bundle mBundle;


    public OptionalArgument(Key<T> key, Bundle bundle)
    {
        mKey = key;
        mBundle = bundle;
    }


    public OptionalArgument(Key<T> key, Fragment fragment)
    {
        this(key, new NullSafe<>(fragment.getArguments()).value(Bundle.EMPTY));
    }


    public OptionalArgument(Key<T> key, Intent intent)
    {
        this(key, new NestedBundle(intent).get());
    }


    public OptionalArgument(Key<T> key, Activity activity)
    {
        this(key, activity.getIntent());
    }


    public OptionalArgument(Key<T> key, Optional<Bundle> optBundle)
    {
        this(key, optBundle.value(Bundle.EMPTY));
    }


    @Override
    public boolean isPresent()
    {
        return mBundle.getParcelable(mKey.name()) != null;
    }


    @Override
    public T value(T defaultValue)
    {
        Box<T> box = mBundle.getParcelable(mKey.name());
        return box != null ? box.content() : defaultValue;
    }


    @Override
    public T value() throws NoSuchElementException
    {
        Box<T> box = mBundle.getParcelable(mKey.name());
        if (box == null)
        {
            throw new NoSuchElementException(String.format("Argument with key `%s` not found in Bundle", mKey.name()));
        }
        return box.content();
    }

}
