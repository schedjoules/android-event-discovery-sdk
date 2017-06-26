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

package com.schedjoules.eventdiscovery.framework.utils.fragment;

import android.support.v4.app.Fragment;


/**
 * {@link FragmentContainer} for a child Fragment in a parent Fragment.
 *
 * @author Gabor Keszthelyi
 */
public final class ChildFragmentContainer implements FragmentContainer
{
    private final Fragment mParentFragment;
    private final int mContainerResId;


    public ChildFragmentContainer(Fragment parentFragment, int containerResId)
    {
        mParentFragment = parentFragment;
        mContainerResId = containerResId;
    }


    @Override
    public void add(Fragment fragment)
    {
        new Add(mContainerResId, fragment).commit(mParentFragment);
    }


    @Override
    public void replace(Fragment fragment)
    {
        new Replace(mContainerResId, fragment).commit(mParentFragment);
    }
}
