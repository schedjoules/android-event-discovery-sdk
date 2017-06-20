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

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.schedjoules.eventdiscovery.R;

import org.dmfs.android.microfragments.transitions.FragmentTransition;


/**
 * {@link FragmentTransition} decorator to add animation:
 * Fades the old fragment out to reveal the new one.
 * Exiting is fade-out fade-in.
 *
 * @author Gabor Keszthelyi
 */
public final class Revealed extends AbstractAnimated
{
    public Revealed(@NonNull FragmentTransition delegate)
    {
        super(delegate,
                R.anim.microfragments_none,
                R.anim.microfragments_fade_exit,
                R.anim.microfragments_fade_enter,
                R.anim.microfragments_fade_exit);
    }


    private Revealed(Parcel in)
    {
        super(in);
    }


    public final static Creator<Revealed> CREATOR = new Creator<Revealed>()
    {
        @Override
        public Revealed createFromParcel(Parcel in)
        {
            return new Revealed(in);
        }


        @Override
        public Revealed[] newArray(int size)
        {
            return new Revealed[size];
        }
    };
}
