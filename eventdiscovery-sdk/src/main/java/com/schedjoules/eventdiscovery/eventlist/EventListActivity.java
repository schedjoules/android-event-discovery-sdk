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

package com.schedjoules.eventdiscovery.eventlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.AdapterNotifier;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsImpl;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsProvider;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.EventListItemsProviderImpl;
import com.schedjoules.eventdiscovery.eventlist.itemsprovider.FlexibleAdapterNotifier;
import com.schedjoules.eventdiscovery.eventlist.view.EvenListScreenView;
import com.schedjoules.eventdiscovery.eventlist.view.EvenListScreenViewImpl;
import com.schedjoules.eventdiscovery.eventlist.view.EventListBackgroundMessage;
import com.schedjoules.eventdiscovery.eventlist.view.EventListLoadingIndicatorOverlay;
import com.schedjoules.eventdiscovery.framework.retain.RealRetainedObjects;
import com.schedjoules.eventdiscovery.framework.retain.Restoring;
import com.schedjoules.eventdiscovery.framework.retain.RetainedObjects;
import com.schedjoules.eventdiscovery.location.LastSelectedLocation;
import com.schedjoules.eventdiscovery.location.LocationSelection;
import com.schedjoules.eventdiscovery.location.LocationSelectionResult;
import com.schedjoules.eventdiscovery.location.PlacesApiLocationSelection;
import com.schedjoules.eventdiscovery.location.SharedPrefLastSelectedLocation;
import com.schedjoules.eventdiscovery.service.ApiService;
import com.schedjoules.eventdiscovery.utils.BaseActivity;
import com.schedjoules.eventdiscovery.utils.FutureLocalServiceConnection;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;
import com.schedjoules.eventdiscovery.utils.InsightsTask;

import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.rfc5545.DateTime;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

import static com.schedjoules.eventdiscovery.EventIntents.EXTRA_GEOLOCATION;
import static com.schedjoules.eventdiscovery.EventIntents.EXTRA_START_AFTER_TIMESTAMP;


/**
 * Activity for the Event List screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListActivity extends BaseActivity implements EvenListScreenView.UserActionListener, LocationSelection.Listener
{
    private FutureServiceConnection<ApiService> mApiService;
    private EvenListScreenView mScreenView;

    private PlacesApiLocationSelection mLocationSelection;
    private EventListItemsProvider mListItemsProvider;

    private LastSelectedLocation mLastSelectedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // TODO It's only Toolbar now inside ScreenView, make it declarative, move rest (related to list) to elsewhere
        mScreenView = new EvenListScreenViewImpl(this);
        mScreenView.init();

        mApiService = new FutureLocalServiceConnection<>(this,
                new Intent("com.schedjoules.API").setPackage(getPackageName()));

        RetainedObjects retainedObjects = new Restoring(getLastCustomNonConfigurationInstance());
        mListItemsProvider = retainedObjects.getOr(0, new EventListItemsProviderImpl(new EventListItemsImpl()));

        mListItemsProvider.setApiService(mApiService);
        mListItemsProvider.setBackgroundMessageUI(new EventListBackgroundMessage(this));
        mListItemsProvider.setLoadingIndicatorOverlayUI(new EventListLoadingIndicatorOverlay(this));

        mLocationSelection = new PlacesApiLocationSelection(this);
        mLocationSelection.registerListener(this);

        mLastSelectedLocation = retainedObjects.getOr(1, new SharedPrefLastSelectedLocation(getApplicationContext()));

        mScreenView.setUserActionListener(this);

        mScreenView.setToolbarTitle(mLastSelectedLocation.get().name());
        mScreenView.setBottomReachScrollListener(mListItemsProvider);

        setupListAdapter();

        if (!retainedObjects.hasAny())
        {
            mListItemsProvider.loadEvents(location(), startAfter());
        }

        new InsightsTask(this).execute(new Screen(new StringToken("list")));
    }


    private void setupListAdapter()
    {
        // Using FlexibleAdapter with sticky headers:
        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(null);
        adapter.setDisplayHeadersAtStartUp(true);
        adapter.setStickyHeaders(true);
        AdapterNotifier adapterNotifier = new FlexibleAdapterNotifier(adapter);

        // Using GeneralMultiTypeAdapter (no sticky headers):
//        GeneralMultiTypeAdapter adapter = new GeneralMultiTypeAdapter(mListItemsProvider);
//        AdapterNotifier adapterNotifier = new StandardAdapterNotifier(adapter);

        mListItemsProvider.setAdapterNotifier(adapterNotifier);
        mScreenView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return mScreenView.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return mScreenView.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @Override
    public void onToolbarTitleClick()
    {
        mLocationSelection.initiateSelection();
    }


    @Override
    public void onLocationMenuIconClick()
    {
        mLocationSelection.initiateSelection();
    }


    @Override
    public void onUpButtonClick()
    {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mLocationSelection.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onLocationSelected(LocationSelectionResult result)
    {
        mLastSelectedLocation.update(result);
        mScreenView.setToolbarTitle(result.name());
        mListItemsProvider.loadEvents(result.geoLocation(), startAfter());
    }


    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        return new RealRetainedObjects(mListItemsProvider, mLastSelectedLocation);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mApiService.disconnect();
        mLocationSelection.unregisterListener();
    }


    private GeoLocation location()
    {
        if (getIntent().hasExtra(EXTRA_GEOLOCATION))
        {
            // TODO Save location and update Toolbar title with name when Event Discovery for input geo-location is actually supported
            return getIntent().getParcelableExtra(EXTRA_GEOLOCATION);
        }
        else
        {
            return mLastSelectedLocation.get().geoLocation();
        }
    }


    private DateTime startAfter()
    {
        if (getIntent().hasExtra(EXTRA_START_AFTER_TIMESTAMP))
        {
            long startAfterTimeStamp = getIntent().getLongExtra(EXTRA_START_AFTER_TIMESTAMP, 0);
            return new DateTime(startAfterTimeStamp);
        }
        else
        {
            return DateTime.nowAndHere();
        }
    }
}
