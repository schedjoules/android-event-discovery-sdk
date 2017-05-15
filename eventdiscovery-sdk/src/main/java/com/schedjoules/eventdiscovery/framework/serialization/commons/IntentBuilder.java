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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.serialization.core.Boxable;
import com.schedjoules.eventdiscovery.framework.serialization.core.FluentBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;

import org.dmfs.optional.Optional;


/**
 * {@link FluentBuilder} for {@link Intent}.
 *
 * @author Gabor Keszthelyi
 */
public final class IntentBuilder implements FluentBuilder<Intent>
{
    private final Intent mIntent;
    private final FluentBuilder<Bundle> mNestedBundleBuilder;


    public IntentBuilder(Intent intent)
    {
        this(intent, new BundleBuilder(new NestedBundle(intent).get()));
    }


    public IntentBuilder()
    {
        this(new Intent());
    }


    public IntentBuilder(String action)
    {
        this(new Intent(action));
    }


    public IntentBuilder(String action, Uri uri)
    {
        this(new Intent(action, uri));
    }


    private IntentBuilder(Intent intent, FluentBuilder<Bundle> nestedBundleBuilder)
    {
        mIntent = new Intent(intent);
        mNestedBundleBuilder = nestedBundleBuilder;
    }


    @Override
    public Intent build()
    {
        mIntent.putExtra(NestedBundle.NESTED_BUNDLE_KEY, mNestedBundleBuilder.build());
        return new Intent(mIntent);
    }


    @Override
    public <T> FluentBuilder<Intent> with(Key<T> key, Box<T> box)
    {
        return new IntentBuilder(mIntent, mNestedBundleBuilder.with(key, box));
    }


    @Override
    public <T> FluentBuilder<Intent> with(Key<T> key, Optional<Box<T>> optBox)
    {
        if (!optBox.isPresent())
        {
            return this;
        }
        return with(key, optBox.value());
    }


    @Override
    public <T extends Boxable> FluentBuilder<Intent> withBoxable(Key<T> key, Optional<T> optBoxable)
    {
        if (!optBoxable.isPresent())
        {
            return this;
        }
        return with(key, optBoxable.value().boxed());
    }

}
