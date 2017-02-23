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

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentLocationSelectionBinding;
import com.schedjoules.eventdiscovery.framework.EventIntents;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.list.GeneralMultiTypeAdapter;
import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.location.model.GeoPlace;
import com.schedjoules.eventdiscovery.framework.location.model.ParcelableGeoPlace;
import com.schedjoules.eventdiscovery.framework.location.recentlocations.RecentLocationsModule;
import com.schedjoules.eventdiscovery.framework.location.recentlocations.Remembered;
import com.schedjoules.eventdiscovery.framework.searchlist.BasicSearchListItems;
import com.schedjoules.eventdiscovery.framework.searchlist.CompositeSearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchListItems;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModulesFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.delaying.UpdateDelaying;
import com.schedjoules.eventdiscovery.framework.widgets.AbstractTextWatcher;
import com.schedjoules.eventdiscovery.framework.widgets.HideKeyboardActionListener;

import java.util.Arrays;
import java.util.List;


/**
 * Fragment for the location selection.
 *
 * @author Gabor Keszthelyi
 */
public final class LocationSelectionFragment extends BaseFragment
{
    private EditText mSearchEditText;
    private SearchModule mCompositeModule;
    private GeneralMultiTypeAdapter mAdapter;
    private SearchListItems mSearchListItems;


    public static Fragment newInstance()
    {
        return new LocationSelectionFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mSearchListItems = new UpdateDelaying(new BasicSearchListItems(), 5, getResources().getInteger(android.R.integer.config_mediumAnimTime));
        mAdapter = new GeneralMultiTypeAdapter(mSearchListItems);
        mSearchListItems.setAdapter(mAdapter);

        // SearchModuleFactory can be made Serializable/Parcelable so they can be passed in
        List<SearchModule> modules = new SearchModulesFactory<>(
                getActivity(),
                mSearchListItems,
                new ItemChosenAction<GeoPlace>()
                {
                    @Override
                    public void onItemChosen(GeoPlace geoPlace)
                    {
                        Bundle nestedExtras = new Bundle();
                        nestedExtras.putParcelable(EventIntents.EXTRA_GEO_PLACE, new ParcelableGeoPlace(geoPlace));
                        Intent data = new Intent();
                        data.putExtra("com.schedjoules.nestedExtras", nestedExtras);
                        getActivity().setResult(Activity.RESULT_OK, data);
                        getActivity().finish();
                    }
                },
                Arrays.asList(
                        new CurrentLocationPermissionProxyFactory(CurrentLocationModule.FACTORY),
                        new Remembered(RecentLocationsModule.FACTORY),
                        new Remembered(PlaceSuggestionModule.FACTORY))
        ).create();

        mCompositeModule = new CompositeSearchModule(modules);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        SchedjoulesFragmentLocationSelectionBinding views = DataBindingUtil.inflate(inflater,
                R.layout.schedjoules_fragment_location_selection, container, false);

        initToolbar(views.schedjoulesLocationSelectionToolbar);

        views.schedjoulesLocationSelectionList.setAdapter(mAdapter);

        mSearchEditText = views.schedjoulesLocationSelectionInput;
        mSearchEditText.addTextChangedListener(new AbstractTextWatcher()
        {
            @Override
            public void afterTextChanged(Editable editable)
            {
                String newQuery = editable.toString();
                mSearchListItems.onSearchQueryChange(newQuery);
                mCompositeModule.onSearchQueryChange(newQuery);
            }
        });
        mSearchEditText.setOnEditorActionListener(new HideKeyboardActionListener());

        if (savedInstanceState == null)
        {
            mSearchListItems.onSearchQueryChange("");
            mCompositeModule.onSearchQueryChange("");
        }

        return views.getRoot();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        // update all modules, things may have changed while we were asleep.
        // for instance the user could have revoked or granted a permission in the device settings
        mCompositeModule.onSearchQueryChange(mSearchEditText.getText().toString());
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
    public void onDestroy()
    {
        super.onDestroy();
        mCompositeModule.shutDown();
    }

}
