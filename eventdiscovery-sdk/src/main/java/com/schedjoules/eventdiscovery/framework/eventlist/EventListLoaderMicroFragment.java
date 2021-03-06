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

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;

import com.schedjoules.client.eventsdiscovery.Category;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.EventsDiscovery;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.client.eventsdiscovery.queries.CategoriesQuery;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventListLoaderBinding;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.common.FirstResultPageHolder;
import com.schedjoules.eventdiscovery.framework.common.FluentContextState;
import com.schedjoules.eventdiscovery.framework.locationpicker.SharedPrefLastSelectedPlace;
import com.schedjoules.eventdiscovery.framework.model.category.EagerCategories;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.CategoriesBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.splash.SplashErrorMicroFragment;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJob;
import com.schedjoules.eventdiscovery.framework.utils.SimpleServiceJobQueue;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.transitions.Faded;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.optional.Optional;
import org.dmfs.rfc5545.DateTime;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * {@link MicroFragment} for the loading screen before showing the event list.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListLoaderMicroFragment implements MicroFragment<Bundle>
{
    private final Bundle mArgs;


    public EventListLoaderMicroFragment(Bundle args)
    {
        mArgs = args;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        throw new UnsupportedOperationException("Title is not used for this fragment");
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host)
    {
        return new LoaderFragment();
    }


    @NonNull
    @Override
    public Bundle parameter()
    {
        return mArgs;
    }


    @Override
    public boolean skipOnBack()
    {
        return true;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeBundle(mArgs);
    }


    public static final Creator<EventListLoaderMicroFragment> CREATOR = new Parcelable.Creator<EventListLoaderMicroFragment>()
    {
        @Override
        public EventListLoaderMicroFragment createFromParcel(Parcel in)
        {
            return new EventListLoaderMicroFragment(in.readBundle(getClass().getClassLoader()));
        }


        @Override
        public EventListLoaderMicroFragment[] newArray(int size)
        {
            return new EventListLoaderMicroFragment[size];
        }
    };


    public static final class LoaderFragment extends BaseFragment
    {
        private final Timestamp mTimestamp = new UiTimestamp();

        private SimpleServiceJobQueue<ApiService> mApiServiceJobQueue;
        private ProgressBar mProgressBar;

        private AtomicBoolean mResultPageLoaded = new AtomicBoolean();
        private AtomicBoolean mCategoriesLoaded = new AtomicBoolean();
        private Bundle mIncomingArgs;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mApiServiceJobQueue = new SimpleServiceJobQueue<>(new ApiService.FutureConnection(getActivity()));
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            SchedjoulesFragmentEventListLoaderBinding views = DataBindingUtil.inflate(inflater,
                    R.layout.schedjoules_fragment_event_list_loader, container, false);
            mProgressBar = views.schedjoulesEventListProgressBar;

            mIncomingArgs = new FragmentEnvironment<Bundle>(this).microFragment().parameter();

            // Showing the ProgressBar with a little delay and fade-in to avoid abrupt and momentarily frozen appearing
            AlphaAnimation anim = new AlphaAnimation(0, 1);
            anim.setDuration(400);
            anim.setStartOffset(200);
            mProgressBar.startAnimation(anim);

            return views.getRoot();
        }


        @Override
        public void onResume()
        {
            super.onResume();
            Optional<DateTime> startAfter = new OptionalArgument<>(Keys.DATE_TIME_START_AFTER, mIncomingArgs);
            GeoLocation location = new SharedPrefLastSelectedPlace(getContext()).get().geoLocation();
            final EventsDiscovery query = new EventsDiscoveryFactory(startAfter, location).create();

            mApiServiceJobQueue.post(new ServiceJob<ApiService>()
            {
                @Override
                public void execute(ApiService service)
                {
                    try
                    {
                        ResultPage<Envelope<Event>> resultPage = service.apiResponse(query);
                        FirstResultPageHolder.set(resultPage);
                        mResultPageLoaded.set(true);
                        loadReady();
                    }
                    catch (ProtocolError | IOException | ProtocolException | URISyntaxException | RuntimeException e)
                    {
                        Log.e("EventListLoaderMF", "Failed to load first page", e);
                        onError();
                    }
                }


                @Override
                public void onTimeOut()
                {
                    onError();
                }

            }, 5000);

            mApiServiceJobQueue.post(new ServiceJob<ApiService>()
            {
                @Override
                public void execute(ApiService service)
                {
                    try
                    {
                        Iterable<Category> categories = service.apiResponse(new CategoriesQuery());

                        new FluentContextState(getActivity()).put(Keys.CATEGORIES, new CategoriesBox(new EagerCategories(categories)));

                        mCategoriesLoaded.set(true);
                        loadReady();
                    }
                    catch (ProtocolError | IOException | ProtocolException | URISyntaxException | RuntimeException e)
                    {
                        Log.e("EventListLoaderMF", "Failed to load category labels", e);
                        onError();
                    }
                }


                @Override
                public void onTimeOut()
                {
                    onError();
                }
            }, 5000);
        }


        private void onError()
        {
            startTransition(new Faded(new ForwardTransition<>(
                    new SplashErrorMicroFragment(new FragmentEnvironment<Bundle>(LoaderFragment.this).microFragment().parameter()),
                    mTimestamp)));
        }


        @Override
        public void onDestroy()
        {
            mApiServiceJobQueue.disconnect();
            super.onDestroy();
        }


        private void loadReady()
        {
            if (mResultPageLoaded.get() && mCategoriesLoaded.get())
            {
                startTransition(new Faded(new ForwardTransition(new EventListMicroFragment(mIncomingArgs), mTimestamp)));
            }
        }


        private void startTransition(FragmentTransition fragmentTransition)
        {
            if (isResumed())
            {
                // Hiding ProgressBar at this point because it strangely 'show up on next screen' at a slightly different position otherwise
                mProgressBar.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

                new FragmentEnvironment<>(this).host().execute(getActivity(), fragmentTransition);
            }
        }
    }

}
