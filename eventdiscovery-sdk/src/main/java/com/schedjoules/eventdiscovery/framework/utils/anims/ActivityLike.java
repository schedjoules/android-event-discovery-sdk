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
 * {@link FragmentTransition} decorator to add animation using the standard Activity transition animations.
 *
 * @author Gabor Keszthelyi
 */
public final class ActivityLike extends AbstractAnimated
{
    public ActivityLike(@NonNull FragmentTransition delegate)
    {
        super(delegate,
                R.anim.schedjoules_activity_open_enter,
                R.anim.schedjoules_activity_open_exit,
                R.anim.schedjoules_activity_close_enter,
                R.anim.schedjoules_activity_close_exit);
    }


    private ActivityLike(Parcel in)
    {
        super(in);
    }


    public final static Creator<ActivityLike> CREATOR = new Creator<ActivityLike>()
    {
        @Override
        public ActivityLike createFromParcel(Parcel in)
        {
            return new ActivityLike(in);
        }


        @Override
        public ActivityLike[] newArray(int size)
        {
            return new ActivityLike[size];
        }
    };
}
