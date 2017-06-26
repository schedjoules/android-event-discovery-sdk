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
import android.support.v4.app.Fragment;

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.serialization.core.Boxable;
import com.schedjoules.eventdiscovery.framework.serialization.core.FluentBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;

import org.dmfs.optional.Optional;


/**
 * Convenience {@link FluentBuilder} for creating {@link Fragment} with args.
 * <p>
 * Note: Not immutable, the same Fragment instance is returned with {@link #build()} as received in the constructor.
 *
 * @author Gabor Keszthelyi
 */
public final class FragmentBuilder implements FluentBuilder<Fragment>
{
    private final Fragment mFragment;
    private final FluentBuilder<Bundle> mBundleBuilder;


    public FragmentBuilder(Fragment fragment)
    {
        this(fragment, new BundleBuilder());
    }


    private FragmentBuilder(Fragment fragment, FluentBuilder<Bundle> bundleBuilder)
    {
        mFragment = fragment;
        mBundleBuilder = bundleBuilder;
    }


    @Override
    public Fragment build()
    {
        mFragment.setArguments(mBundleBuilder.build());
        return mFragment;
    }


    @Override
    public <T> FluentBuilder<Fragment> with(Key<T> key, Box<T> box)
    {
        return new FragmentBuilder(mFragment, mBundleBuilder.with(key, box));
    }


    @Override
    public <T> FluentBuilder<Fragment> with(Key<T> key, Optional<Box<T>> optBox)
    {
        return new FragmentBuilder(mFragment, mBundleBuilder.with(key, optBox));
    }


    @Override
    public <T extends Boxable> FluentBuilder<Fragment> withBoxable(Key<T> key, Optional<T> optBoxable)
    {
        return new FragmentBuilder(mFragment, mBundleBuilder.withBoxable(key, optBoxable));
    }
}
