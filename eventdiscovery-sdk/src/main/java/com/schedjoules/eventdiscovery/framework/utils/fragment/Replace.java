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

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


/**
 * {@link FragmentTransaction} to replace a {@link Fragment}.
 *
 * @author Gabor Keszthelyi
 */
public final class Replace implements FragmentTransaction
{
    private final int mContainerResId;
    private final Fragment mFragment;


    public Replace(@IdRes int containerResId, Fragment fragment)
    {
        mContainerResId = containerResId;
        mFragment = fragment;
    }


    @Override
    public void commit(FragmentActivity activity)
    {
        doCommit(activity.getSupportFragmentManager());
    }


    @Override
    public void commit(Fragment fragment)
    {
        doCommit(fragment.getChildFragmentManager());
    }


    private void doCommit(FragmentManager fm)
    {
        fm.beginTransaction().replace(mContainerResId, mFragment).commit();
    }
}
