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

import android.os.Bundle;

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.serialization.core.Boxable;
import com.schedjoules.eventdiscovery.framework.serialization.core.FluentBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;

import org.dmfs.optional.Optional;


/**
 * {@link FluentBuilder} for {@link Bundle}.
 *
 * @author Gabor Keszthelyi
 */
public final class BundleBuilder implements FluentBuilder<Bundle>
{
    private final Bundle mBundle;


    public BundleBuilder()
    {
        this(Bundle.EMPTY, false);
    }


    public BundleBuilder(Bundle bundle)
    {
        this(bundle, true);
    }


    private BundleBuilder(Bundle bundle, boolean clone)
    {
        mBundle = clone ? new Bundle(bundle) : bundle;
    }


    @Override
    public Bundle build()
    {
        return new Bundle(mBundle);
    }


    @Override
    public <T> FluentBuilder<Bundle> with(Key<T> key, Box<T> box)
    {
        Bundle bundle = new Bundle(mBundle);
        bundle.putParcelable(key.name(), box);
        // Use this to test parcelling during development:
        // bundle.putParcelable(key.name(), ParcellingEnforcer.immediateDeepParcel(box));
        return new BundleBuilder(bundle, false);
    }


    @Override
    public <T> FluentBuilder<Bundle> with(Key<T> key, Optional<Box<T>> optBox)
    {
        if (!optBox.isPresent())
        {
            return this;
        }
        return with(key, optBox.value());
    }


    @Override
    public <T extends Boxable> FluentBuilder<Bundle> withBoxable(Key<T> key, Optional<T> optBoxable)
    {
        if (!optBoxable.isPresent())
        {
            return this;
        }
        return with(key, optBoxable.value().boxed());
    }

}
