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

package com.schedjoules.eventdiscovery.location;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.schedjoules.eventdiscovery.EventIntents;
import com.schedjoules.eventdiscovery.framework.listen.OnActivityResult;
import com.schedjoules.eventdiscovery.location.model.ParcelableGeoPlace;


/**
 * TODO better name, think about design
 * <p>
 * {@link LocationSelection} that uses the Google Places API.
 *
 * @author Gabor Keszthelyi
 */
public final class CustomLocationSelection implements LocationSelection, OnActivityResult
{
    private static final int REQUEST_CODE = 4731;

    private final Fragment mFragment;
    private Listener mListener;


    public CustomLocationSelection(Fragment fragment)
    {
        mFragment = fragment;
    }


    @Override
    public void registerListener(Listener listener)
    {
        mListener = listener;
    }


    @Override
    public void unregisterListener()
    {
        mListener = null;
    }


    @Override
    public void initiateSelection()
    {
        Intent intent = new Intent(mFragment.getActivity(), LocationSelectionActivity.class);
        mFragment.startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && mFragment.isAdded())
        {
            ParcelableGeoPlace geoPlace = data.getParcelableExtra(EventIntents.EXTRA_GEO_PLACE);
            if (mListener != null)
            {
                mListener.onPlaceSelected(geoPlace);
            }
        }
        // Activity.RESULT_CANCELED -> user canceled the operation, we do nothing
    }
}

