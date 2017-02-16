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

package com.schedjoules.eventdiscovery.framework.location.model;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.schedjoules.eventdiscovery.framework.location.model.namedplace.NamedPlace;


/**
 * {@link NamedPlace} adapting {@link AutocompletePrediction}.
 *
 * @author Gabor Keszthelyi
 */
public final class GooglePredictionNamedPlace implements NamedPlace
{
    private final AutocompletePrediction mPrediction;


    public GooglePredictionNamedPlace(AutocompletePrediction prediction)
    {
        mPrediction = prediction;
    }


    @Override
    public String id()
    {
        return mPrediction.getPlaceId();
    }


    @Override
    public CharSequence name()
    {
        return mPrediction.getPrimaryText(null);
    }


    @Override
    public CharSequence extraContext()
    {
        return mPrediction.getSecondaryText(null);
    }
}
