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

package com.schedjoules.eventdiscovery.framework.location;

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
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentLocationSelectionBinding;
import com.schedjoules.eventdiscovery.framework.EventIntents;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.list.GeneralMultiTypeAdapter;
import com.schedjoules.eventdiscovery.framework.list.changes.BasicChangeableListItems;
import com.schedjoules.eventdiscovery.framework.list.changes.ChangeableListItems;
import com.schedjoules.eventdiscovery.framework.location.list.PlaceListController;
import com.schedjoules.eventdiscovery.framework.location.list.PlaceListControllerImpl;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.ParcelableGeoPlace;
import com.schedjoules.eventdiscovery.framework.widgets.AbstractTextWatcher;
import com.schedjoules.eventdiscovery.framework.widgets.HideKeyboardActionListener;


/**
 * Fragment for the location selection.
 *
 * @author Gabor Keszthelyi
 */
public final class LocationSelectionFragment extends BaseFragment implements PlaceListController.PlaceSelectedListener
{
    private GoogleApiClient mGoogleApiClient;
    private PlaceListController mPlaceListController;
    private ChangeableListItems mSuggestionItems;


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
                .Builder(getContext().getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        /**
         * TODO enableAutomanage OR manual error handling
         * enableAutoManage() on the builder would enable automatic default error handling as well,
         * but it's tricky to get initialization correctly with Activity and retained Fragment lifecycles.
         * Either enable automanage or add 'manual' error handling with addOnConnectionFailedListener().
         *
         * See https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient.Builder.html#enableAutoManage(android.support.v4.app.FragmentActivity, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener)
         */
        mGoogleApiClient.connect();

        mSuggestionItems = new BasicChangeableListItems();
        mPlaceListController = new PlaceListControllerImpl(mGoogleApiClient, this, mSuggestionItems);
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
        mSuggestionItems.setAdapter(adapter);

        views.schedjoulesLocationSelectionList.setAdapter(adapter);

        EditText searchEditText = views.schedjoulesLocationSelectionInput;
        searchEditText.addTextChangedListener(new AbstractTextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                mPlaceListController.query(s.toString());
            }
        });
        searchEditText.setOnEditorActionListener(new HideKeyboardActionListener());

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
            Bundle nestedExtras = new Bundle();
            nestedExtras.putParcelable(EventIntents.EXTRA_GEO_PLACE, new ParcelableGeoPlace(geoPlace));
            Intent data = new Intent();
            data.putExtra("com.schedjoules.nestedExtras", nestedExtras);
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }
}
