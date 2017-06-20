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

package com.schedjoules.eventdiscovery.framework.utils.anims;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.transitions.FragmentTransition;


/**
 * Abstract class for {@link FragmentTransition}s decorators that add animations.
 *
 * @author Gabor Keszthelyi
 */
public abstract class AbstractAnimated implements FragmentTransition
{
    private final FragmentTransition mDelegate;
    private final int mEnter;
    private final int mExit;
    private final int mPopEnter;
    private final int mPopExit;


    protected AbstractAnimated(@NonNull FragmentTransition delegate,
                               @AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit)
    {
        mDelegate = delegate;
        mEnter = enter;
        mExit = exit;
        mPopEnter = popEnter;
        mPopExit = popExit;
    }


    protected AbstractAnimated(Parcel in)
    {
        this((FragmentTransition) in.readParcelable(AbstractAnimated.class.getClassLoader()),
                in.readInt(), in.readInt(), in.readInt(), in.readInt());
    }


    @NonNull
    @Override
    public final Timestamp timestamp()
    {
        return mDelegate.timestamp();
    }


    @Override
    public final void prepare(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        mDelegate.prepare(context, fragmentManager, host, previousStep);
    }


    @NonNull
    @Override
    public final FragmentTransaction updateTransaction(@NonNull Context context, @NonNull FragmentTransaction fragmentTransaction, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        fragmentTransaction.setCustomAnimations(mEnter, mExit, mPopEnter, mPopExit);
        return mDelegate.updateTransaction(context, fragmentTransaction, fragmentManager, host, previousStep);
    }


    @Override
    public final void cleanup(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull MicroFragmentHost host, @NonNull MicroFragment<?> previousStep)
    {
        mDelegate.cleanup(context, fragmentManager, host, previousStep);
    }


    @Override
    public final int describeContents()
    {
        return 0;
    }


    @Override
    public final void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mDelegate, flags);
        dest.writeInt(mEnter);
        dest.writeInt(mExit);
        dest.writeInt(mPopEnter);
        dest.writeInt(mPopExit);
    }

}
