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
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.EventIntents;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.BaseActivity;
import com.schedjoules.eventdiscovery.common.BaseFragment;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentLocationSelectionBinding;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.StandardAdapterNotifier;
import com.schedjoules.eventdiscovery.framework.adapter.GeneralMultiTypeAdapter;
import com.schedjoules.eventdiscovery.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.location.model.ParcelableGeoPlace;
import com.schedjoules.eventdiscovery.widgets.SimpleTextWatcher;


/**
 * Fragment for the location selection.
 *
 * @author Gabor Keszthelyi
 */
public final class LocationSelectionFragment extends BaseFragment implements LocationItemsProvider.PlaceSelectedListener
{
    // TODO change back
    public static GoogleApiClient mGoogleApiClient;
    private LocationItemsProviderImpl mLocationItemsProvider;


    public static Fragment newInstance()
    {
        return new LocationSelectionFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener()
                {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
                    {
                        // TODO
                    }
                })
                .build();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        SchedjoulesFragmentLocationSelectionBinding views = DataBindingUtil.inflate(inflater,
                R.layout.schedjoules_fragment_location_selection, container, false);

        initToolbar(views.schedjoulesLocationSelectionToolbar);

        initList(views.schedjoulesLocationSelectionList);

        views.schedjoulesLocationSelectionInput.addTextChangedListener(new SimpleTextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                mLocationItemsProvider.query(s.toString());
            }
        });

        return views.getRoot();
    }


    private void initToolbar(Toolbar toolbar)
    {
        setHasOptionsMenu(true);
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initList(RecyclerView recyclerView)
    {
        mLocationItemsProvider = new LocationItemsProviderImpl(mGoogleApiClient, this);
        GeneralMultiTypeAdapter adapter = new GeneralMultiTypeAdapter(mLocationItemsProvider);
        mLocationItemsProvider.setAdapterNotifier(new StandardAdapterNotifier(adapter));
        recyclerView.setAdapter(adapter);
    }

    // TODO decide if SearchView or EditText is better
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.schedjoules_location_selection, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.schedjoules_location_search).getActionView();
        searchView.requestFocus();
        searchView.setIconifiedByDefault(false); // Expand by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // TODO review what we should do here
                mLocationItemsProvider.query(query);
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText)
            {
                mLocationItemsProvider.query(newText);
                return true;
            }
        });
    }
*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            // TODO revisit
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPlaceSelected(GeoPlace geoPlace)
    {
        Intent data = new Intent();

        ParcelableGeoPlace value = new ParcelableGeoPlace(geoPlace);
        data.putExtra(EventIntents.EXTRA_NAMED_LOCATION,
                value);
        // TODO null checks
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }
}
