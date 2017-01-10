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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.EventIntents;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.BaseActivity;
import com.schedjoules.eventdiscovery.common.BaseFragment;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentLocationSelectionBinding;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.StandardAdapterNotifier;
import com.schedjoules.eventdiscovery.framework.adapter.GeneralMultiTypeAdapter;
import com.schedjoules.eventdiscovery.location.list.PlaceListController;
import com.schedjoules.eventdiscovery.location.list.PlaceListControllerImpl;
import com.schedjoules.eventdiscovery.location.list.PlaceSuggestionListItemsImpl;
import com.schedjoules.eventdiscovery.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.location.model.ParcelableGeoPlace;
import com.schedjoules.eventdiscovery.widgets.HideKeyboardActionListener;
import com.schedjoules.eventdiscovery.widgets.SimpleTextWatcher;


/**
 * Fragment for the location selection.
 *
 * @author Gabor Keszthelyi
 */
public final class LocationSelectionFragment extends BaseFragment implements PlaceListController.PlaceSelectedListener
{
    private GoogleApiClient mGoogleApiClient;
    private PlaceListController mPlaceListController;
    private PlaceSuggestionListItemsImpl mSuggestionItems;


    public static Fragment newInstance()
    {
        return new LocationSelectionFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        mSuggestionItems = new PlaceSuggestionListItemsImpl();
        mPlaceListController = new PlaceListControllerImpl(mGoogleApiClient, this, mSuggestionItems);

        // TODO No Play Services error handling? Seems to be handled by default, but look into it.
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        SchedjoulesFragmentLocationSelectionBinding views = DataBindingUtil.inflate(inflater,
                R.layout.schedjoules_fragment_location_selection, container, false);

        initToolbar(views.schedjoulesLocationSelectionToolbar);

        // New Adapter is needed after configuration change, otherwise activity is leaked
        GeneralMultiTypeAdapter adapter = new GeneralMultiTypeAdapter(mSuggestionItems);
        mSuggestionItems.setAdapterNotifier(new StandardAdapterNotifier(adapter));

        views.schedjoulesLocationSelectionList.setAdapter(adapter);

        EditText editText = views.schedjoulesLocationSelectionInput;
        editText.addTextChangedListener(new SimpleTextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                mPlaceListController.query(s.toString());
            }
        });
        editText.setOnEditorActionListener(new HideKeyboardActionListener());

        return views.getRoot();
    }


    private void initToolbar(Toolbar toolbar)
    {
        setHasOptionsMenu(true);
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home && getActivity() != null)
        {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPlaceSelected(GeoPlace geoPlace)
    {
        if (getActivity() != null)
        {
            Intent data = new Intent();
            data.putExtra(EventIntents.EXTRA_GEO_PLACE, new ParcelableGeoPlace(geoPlace));
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }


    @Override
    public void onDestroy()
    {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

}
