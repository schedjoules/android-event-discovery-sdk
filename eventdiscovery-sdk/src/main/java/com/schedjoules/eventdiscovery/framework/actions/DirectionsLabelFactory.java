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

package com.schedjoules.eventdiscovery.framework.actions;

import android.content.Context;

import com.schedjoules.client.eventsdiscovery.Location;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.utils.AddressName;
import com.schedjoules.eventdiscovery.framework.utils.charsequence.CharSequenceFactory;


/**
 * Creates the label for the Directions action.
 *
 * @author Gabor Keszthelyi
 */
public final class DirectionsLabelFactory implements CharSequenceFactory
{
    private final Iterable<Location> mLocations;


    public DirectionsLabelFactory(Iterable<Location> locations)
    {
        mLocations = locations;
    }


    @Override
    public CharSequence create(Context context)
    {
        return new AddressName(mLocations).value(context.getString(R.string.schedjoules_action_directions).toUpperCase());
    }
}
