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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventDetailsContentLoadingActionsBinding;
import com.schedjoules.eventdiscovery.framework.async.rx.RxEventActions;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.views.EventHeaderView;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.EventBox;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;
import com.schedjoules.eventdiscovery.framework.services.EventService;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJob;
import com.schedjoules.eventdiscovery.framework.utils.ServiceJobQueue;
import com.schedjoules.eventdiscovery.framework.utils.SimpleServiceJobQueue;
import com.schedjoules.eventdiscovery.framework.utils.anims.Revealed;
import com.schedjoules.eventdiscovery.framework.widgets.AccentColoredProgressBar;

import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.Timestamp;
import org.dmfs.android.microfragments.timestamps.UiTimestamp;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.FragmentTransition;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.Link;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import gk.android.investigator.Investigator;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * {@link MicroFragment} which loads the necessary extra data to show the event details screen.
 * <p>
 * It already receives an event with the basic information, so it can show them while loading the rest (rest is event description and actions).
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailLoaderMicroFragment implements MicroFragment<Event>
{
    public final static Creator<EventDetailLoaderMicroFragment> CREATOR = new Creator<EventDetailLoaderMicroFragment>()
    {
        @Override
        public EventDetailLoaderMicroFragment createFromParcel(Parcel source)
        {
            Box<Event> eventBox = source.readParcelable(getClass().getClassLoader());
            return new EventDetailLoaderMicroFragment(eventBox.content());
        }


        @Override
        public EventDetailLoaderMicroFragment[] newArray(int size)
        {
            return new EventDetailLoaderMicroFragment[size];
        }
    };
    private final Event mEvent;


    public EventDetailLoaderMicroFragment(Event event)
    {
        mEvent = event;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        throw new RuntimeException("This Fragment has no title");
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, MicroFragmentHost host)
    {
        return new LoaderFragment();
    }


    @NonNull
    @Override
    public Event parameter()
    {
        return mEvent;
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
        dest.writeParcelable(new EventBox(mEvent), flags);
    }


    public final static class LoaderFragment extends BaseFragment
    {
        private final Timestamp mTimestamp = new UiTimestamp();
        private ServiceJobQueue<EventService> mEventServiceJobQueue;

        private Event mEvent;

        private AtomicReference<Event> mEventWithDetails = new AtomicReference<>();
        private AtomicReference<List<Link>> mActions = new AtomicReference<>();
        private AtomicBoolean mSleepIsOver = new AtomicBoolean();
        private AccentColoredProgressBar mProgressBar;
        private Disposable mDisposable;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mEventServiceJobQueue = new SimpleServiceJobQueue<>(new EventService.FutureConnection(getActivity()));
            mEvent = new FragmentEnvironment<Event>(this).microFragment().parameter();
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            setStatusBarCoverEnabled(true);

            SchedjoulesFragmentEventDetailsContentLoadingActionsBinding views = DataBindingUtil.inflate(inflater,
                    R.layout.schedjoules_fragment_event_details_content_loading_actions, container, false);

            new EventHeaderView(getActivity(), views.schedjoulesDetailsHeader).update(mEvent);

            mProgressBar = views.schedjoulesEventDetailsHorizontalActionsProgressbar;
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
            Investigator.log(this);

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(300);
                    }
                    catch (InterruptedException e)
                    {
                    }
                    finally
                    {
                        mSleepIsOver.set(true);
                        loaderReady();
                    }
                }
            }).start();

            mEventServiceJobQueue.post(
                    new ServiceJob<EventService>()
                    {
                        @Override
                        public void execute(EventService service)
                        {
                            try
                            {
                                mEventWithDetails.set(service.event(mEvent.uid()));
                                loaderReady();
                            }
                            catch (TimeoutException | InterruptedException | URISyntaxException | ProtocolError | ProtocolException | IOException | RuntimeException e)
                            {
                                Log.e("EventLoaderMF", "Failed to load event: " + mEvent.uid(), e);
                                onLoadingError();
                            }
                        }


                        @Override
                        public void onTimeOut()
                        {
                            onLoadingError();
                        }
                    }, 5000);

            mDisposable = Single.wrap(new RxEventActions(getContext(), mEvent.uid()))
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableSingleObserver<List<Link>>()
                    {

                        @Override
                        public void onSuccess(@io.reactivex.annotations.NonNull List<Link> links)
                        {
                            Investigator.log(this, "links", links);
                            mActions.set(links);
                            loaderReady();
                        }


                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e)
                        {
                            Investigator.log(this, "error", e);
                            onLoadingError();
                        }
                    });
        }


        private void onLoadingError()
        {
            startTransition(new Revealed(new ForwardTransition(new ErrorMicroFragment(), mTimestamp)));
        }


        @Override
        public void onDestroy()
        {
            Investigator.log(this);
            mDisposable.dispose();
            mEventServiceJobQueue.disconnect();
            super.onDestroy();
        }


        private void loaderReady()
        {
            if (mEventWithDetails.get() != null && mActions.get() != null && mSleepIsOver.get())
            {
                startTransition(new Revealed(new ForwardTransition(new ShowEventMicroFragment(mEventWithDetails.get(), mActions.get()), mTimestamp)));
            }
        }


        private void startTransition(FragmentTransition fragmentTransition)
        {
            if (isResumed())
            {
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
