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

package com.schedjoules.eventdiscovery.framework.locationpicker;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentLocationPickerBinding;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.googleapis.BasicGoogleApis;
import com.schedjoules.eventdiscovery.framework.googleapis.Connected;
import com.schedjoules.eventdiscovery.framework.googleapis.GoogleApis;
import com.schedjoules.eventdiscovery.framework.list.GeneralMultiTypeAdapter;
import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.locationpicker.modules.currentlocation.CurrentLocationModuleFactory;
import com.schedjoules.eventdiscovery.framework.locationpicker.modules.currentlocation.CurrentLocationPermissionProxyFactory;
import com.schedjoules.eventdiscovery.framework.locationpicker.modules.placesuggestion.PlaceSuggestionModuleFactory;
import com.schedjoules.eventdiscovery.framework.locationpicker.modules.recentlocations.RecentLocationsModule;
import com.schedjoules.eventdiscovery.framework.locationpicker.modules.recentlocations.Remembered;
import com.schedjoules.eventdiscovery.framework.model.location.geoplace.GeoPlace;
import com.schedjoules.eventdiscovery.framework.searchlist.BasicSearchListItems;
import com.schedjoules.eventdiscovery.framework.searchlist.CompositeSearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchListItems;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModulesFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchQueryInputListener;
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
public final class LocationPickerFragment extends BaseFragment
{
    private EditText mSearchEditText;
    private SearchModule mCompositeModule;
    private GeneralMultiTypeAdapter mAdapter;
    private SearchListItems mSearchListItems;
    private TextWatcher mSearchTextWatcher;


    public static Fragment newInstance()
    {
        return new LocationPickerFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mSearchListItems = new UpdateDelaying(new BasicSearchListItems(), 5, getResources().getInteger(android.R.integer.config_mediumAnimTime));
        mAdapter = new GeneralMultiTypeAdapter(mSearchListItems);
        mSearchListItems.setAdapter(mAdapter);

        GoogleApis googleApis = new Connected(new BasicGoogleApis(getActivity(), LocationServices.API, Places.GEO_DATA_API));

        // SearchModuleFactory can be made Serializable/Parcelable so they can be passed in
        List<SearchModule> modules = new SearchModulesFactory<>(
                getActivity(),
                mSearchListItems,
                new ItemChosenAction<GeoPlace>()
                {
                    @Override
                    public void onItemChosen(GeoPlace geoPlace)
                    {
                        new SharedPrefLastSelectedPlace(getContext()).update(geoPlace);
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                },
                Arrays.asList(
                        new CurrentLocationPermissionProxyFactory(new CurrentLocationModuleFactory(googleApis)),
                        new Remembered(RecentLocationsModule.FACTORY),
                        new Remembered(new PlaceSuggestionModuleFactory(googleApis))
                )
        ).create();

        mCompositeModule = new CompositeSearchModule(modules);

        // Order is relevant here, SearchListItems has to come first:
        mSearchTextWatcher = new SearchTextWatcher(mSearchListItems, mCompositeModule);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        setStatusBarCoverEnabled(false);

        SchedjoulesFragmentLocationPickerBinding views = DataBindingUtil.inflate(inflater,
                R.layout.schedjoules_fragment_location_picker, container, false);

        initToolbar(views.schedjoulesLocationSelectionToolbar);

        views.schedjoulesLocationSelectionList.setAdapter(mAdapter);

        mSearchEditText = views.schedjoulesLocationSelectionInput;
        mSearchEditText.addTextChangedListener(mSearchTextWatcher);
        mSearchEditText.setOnEditorActionListener(new HideKeyboardActionListener());

        return views.getRoot();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        // update all modules, things may have changed while we were asleep.
        // for instance the user could have revoked or granted a permission in the device settings
        mSearchEditText.setText(mSearchEditText.getText()); // triggers the TextChangedListener
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
    public void onDestroyView()
    {
        // remove the TextWatcher to avoid a crash on Android 5.1 which still delivers events after a configuration change under certain conditions.
        mSearchEditText.removeTextChangedListener(mSearchTextWatcher);
        super.onDestroyView();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mCompositeModule.shutDown();
    }


    /**
     * A {@link TextWatcher} which notifies a number of {@link SearchQueryInputListener}s about text changes.
     */
    private final static class SearchTextWatcher extends AbstractTextWatcher
    {
        private final SearchQueryInputListener[] mSearchInputListeners;


        private SearchTextWatcher(SearchQueryInputListener... searchInputListeners)
        {
            mSearchInputListeners = searchInputListeners;
        }


        @Override
        public void afterTextChanged(Editable editable)
        {
            String newQuery = editable.toString();
            for (SearchQueryInputListener listener : mSearchInputListeners)
            {
                listener.onSearchQueryChange(newQuery);
            }
        }
    }
}
