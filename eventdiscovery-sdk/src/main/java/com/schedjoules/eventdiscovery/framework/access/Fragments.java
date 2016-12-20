/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.framework.access;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.schedjoules.eventdiscovery.common.BaseActivity;


/**
 * A convenience wrapper around FragmentManager and common FragmentTransactions used in the app, to reduce boilerplate.
 *
 * @author Gabor Keszthelyi
 */
public final class Fragments
{
    private final FragmentManager mFragmentManager;


    public Fragments(BaseActivity activity)
    {
        mFragmentManager = activity.getSupportFragmentManager();
    }


    public void replace(@IdRes int container, Fragment fragment)
    {
        mFragmentManager.beginTransaction().replace(container, fragment).commit();
    }


    public void add(@IdRes int container, Fragment fragment)
    {
        mFragmentManager.beginTransaction().add(container, fragment).commit();
    }


    public boolean hasNotBeenAddedYet(@IdRes int container)
    {
        return mFragmentManager.findFragmentById(container) == null;
    }

}
