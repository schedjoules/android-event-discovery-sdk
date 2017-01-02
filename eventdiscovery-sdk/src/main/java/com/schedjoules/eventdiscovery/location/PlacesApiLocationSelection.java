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

package com.schedjoules.eventdiscovery.location;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.schedjoules.eventdiscovery.framework.listen.OnActivityResult;


/**
 * {@link LocationSelection} that uses the Google Places API.
 *
 * @author Gabor Keszthelyi
 */
public final class PlacesApiLocationSelection implements LocationSelection, OnActivityResult
{
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 7512;

    private final Fragment mFragment;
    private Listener mListener;


    public PlacesApiLocationSelection(Fragment fragment)
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
        try
        {
            AutocompleteFilter citiesFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(citiesFilter)
                    .build(mFragment.getActivity());

            mFragment.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }
        catch (GooglePlayServicesRepairableException e)
        {
            GoogleApiAvailability.getInstance().getErrorDialog(mFragment.getActivity(), e.getConnectionStatusCode(), 4579).show();
        }
        catch (GooglePlayServicesNotAvailableException e)
        {
            GoogleApiAvailability.getInstance().getErrorDialog(mFragment.getActivity(), e.errorCode, 4573).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && mFragment.isAdded())
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Place place = PlaceAutocomplete.getPlace(mFragment.getActivity(), data);
                if (mListener != null)
                {
                    mListener.onLocationSelected(new GoogleApiNamedLocation(place));
                }
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(mFragment.getActivity(), data);
                Log.e(getClass().getName(), status.getStatusMessage());
            }
            // Activity.RESULT_CANCELED -> user canceled the operation, we do nothing
        }
    }
}

