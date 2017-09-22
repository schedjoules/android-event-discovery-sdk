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

package com.schedjoules.eventdiscovery.framework.eventlist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.eventsdiscovery.Category;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.client.insights.steps.Screen;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListBinding;
import com.schedjoules.eventdiscovery.discovery.SimpleCoverageTest;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.common.ExternalUrlFeedbackForm;
import com.schedjoules.eventdiscovery.framework.common.FirstResultPageHolder;
import com.schedjoules.eventdiscovery.framework.eventlist.view.EventListMenu;
import com.schedjoules.eventdiscovery.framework.filter.FilterFragment;
import com.schedjoules.eventdiscovery.framework.locationpicker.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.CategoryBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.IterableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.FluentBundle;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.utils.InsightsTask;
import com.schedjoules.eventdiscovery.framework.utils.dovecote.BoxDovecote;
import com.schedjoules.eventdiscovery.framework.utils.fragment.Add;
import com.schedjoules.eventdiscovery.framework.utils.fragment.ChildFragmentContainer;
import com.schedjoules.eventdiscovery.framework.utils.fragment.FragmentContainer;
import com.schedjoules.eventdiscovery.framework.utils.loadresult.LoadResult;
import com.schedjoules.eventdiscovery.framework.utils.loadresult.LoadResultException;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.utils.BooleanDovecote;
import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.iterables.EmptyIterable;
import org.dmfs.optional.NullSafe;
import org.dmfs.optional.Optional;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.ParcelableDovecote;
import org.dmfs.pigeonpost.localbroadcast.SerializableDovecote;
import org.dmfs.rfc5545.DateTime;


/**
 * Fragment for showing the event discovery list.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListFragment extends BaseFragment implements EventListMenu.Listener
{
    private EventListMenu mMenu;
    private SchedjoulesFragmentEventListBinding mViews;
    private Dovecote<Boolean> mCoverageDoveCote;
    private Dovecote<LoadResult<ResultPage<Envelope<Event>>>> mEventsLoadDoveCote;
    private Dovecote<Boolean> mReloadDovecote;
    private Dovecote<Box<Iterable<Category>>> mCategoryDovecote;
    private FragmentContainer mListFragmentContainer;
    private boolean mIsInitializing = true;

    private Iterable<Category> mSelectedCategories;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        new InsightsTask(getActivity()).execute(new Screen(new StringToken("list")));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        setStatusBarCoverEnabled(false);

        mViews = DataBindingUtil.inflate(inflater, R.layout.schedjoules_fragment_event_list, container, false);

        mMenu = new EventListMenu(this);
        setHasOptionsMenu(true);

        mSelectedCategories = new OptionalArgument<>(Keys.FILTER_CATEGORIES, new NullSafe<>(savedInstanceState))
                .value(EmptyIterable.<Category>instance());

        mListFragmentContainer = new ChildFragmentContainer(this, R.id.schedjoules_event_list_list_container);

        mCoverageDoveCote = new SerializableDovecote<>(getActivity(), "coverage-test", new Dovecote.OnPigeonReturnCallback<Boolean>()
        {
            @Override
            public void onPigeonReturn(@NonNull Boolean isCovered)
            {
                if (!isCovered)
                {
                    Snackbar.make(mViews.getRoot(), R.string.schedjoules_message_country_not_supported, Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        });
        new SimpleCoverageTest(mCoverageDoveCote).execute(getActivity());

        mEventsLoadDoveCote = new ParcelableDovecote<>(getActivity(), "events-load",
                new Dovecote.OnPigeonReturnCallback<LoadResult<ResultPage<Envelope<Event>>>>()
                {
                    @Override
                    public void onPigeonReturn(@NonNull LoadResult<ResultPage<Envelope<Event>>> loadResult)
                    {
                        try
                        {
                            showResultPage(loadResult.result());
                        }
                        catch (LoadResultException e)
                        {
                            mListFragmentContainer.replace(EventListListErrorFragment.newInstance(mReloadDovecote.cage()));
                        }

                    }
                });

        mReloadDovecote = new BooleanDovecote(getContext(), "reload", new Dovecote.OnPigeonReturnCallback<Boolean>()
        {
            @Override
            public void onPigeonReturn(@NonNull Boolean notUsed)
            {
                load();
            }
        });

        mCategoryDovecote = new BoxDovecote<>(getContext(), Keys.FILTER_CATEGORIES, new Dovecote.OnPigeonReturnCallback<Iterable<Category>>()
        {
            @Override
            public void onPigeonReturn(@NonNull Iterable<Category> categories)
            {
                mSelectedCategories = categories;
                load();
            }
        });

        // savedInstanceState can be null when popping an existing instance from backstack
        if (savedInstanceState == null && mIsInitializing)
        {
            new Add(R.id.schedjoules_event_list_header_container, EventListHeaderFragment.newInstance(mReloadDovecote.cage())).commit(this);
            new Add(R.id.schedjoules_event_list_filter_container, FilterFragment.newInstance(mCategoryDovecote.cage())).commit(this);

            Optional<ResultPage<Envelope<Event>>> firstPage = FirstResultPageHolder.getAndClear();
            if (firstPage.isPresent())
            {
                showResultPage(firstPage.value());
            }
            else
            {
                load();
            }

        }

        mIsInitializing = false;
        return mViews.getRoot();
    }


    private void load()
    {
        GeoLocation location = new SharedPrefLastSelectedPlace(getContext()).get().geoLocation();
        Optional<DateTime> startAfter = new OptionalArgument<>(Keys.DATE_TIME_START_AFTER,
                new FragmentEnvironment<Bundle>(EventListFragment.this).microFragment().parameter());
        ApiQuery<ResultPage<Envelope<Event>>> query = new EventsDiscoveryFactory(startAfter, location, mSelectedCategories).create();

        mListFragmentContainer.replace(EventListListLoaderFragment.newInstance(query, mEventsLoadDoveCote.cage()));
    }


    private void showResultPage(ResultPage<Envelope<Event>> resultPage)
    {
        mListFragmentContainer.replace(resultPage.iterator().hasNext() ?
                EventListListShowFragment.newInstance(resultPage) : EventListListNoEventsFragment.newInstance());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        mMenu.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return mMenu.onOptionsItemSelected(item);
    }


    @Override
    public void onUpButtonClick()
    {
        // TODO revisit this if navigation/screen layouts change (tablet)
        if (getActivity() != null)
        {
            getActivity().finish();
        }
    }


    @Override
    public void onFeedbackMenuClick()
    {
        getView().post(new Runnable()
        {
            @Override
            public void run()
            {
                new ExternalUrlFeedbackForm().show(getActivity(),
                        new FragmentEnvironment<>(EventListFragment.this).host());
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        new FluentBundle(outState).put(Keys.FILTER_CATEGORIES, new IterableBox<>(mSelectedCategories, CategoryBox.FACTORY));
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroyView()
    {
        mCoverageDoveCote.dispose();
        mEventsLoadDoveCote.dispose();
        mReloadDovecote.dispose();
        mCategoryDovecote.dispose();
        super.onDestroyView();
    }

}
