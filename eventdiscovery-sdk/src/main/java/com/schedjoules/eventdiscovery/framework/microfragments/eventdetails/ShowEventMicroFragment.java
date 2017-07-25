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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.EventDetailFragment;
import com.schedjoules.eventdiscovery.framework.model.BasicEnrichedEvent;
import com.schedjoules.eventdiscovery.framework.model.EnrichedEvent;
import com.schedjoules.eventdiscovery.framework.model.ParcelableLink;
import com.schedjoules.eventdiscovery.framework.model.event.ParcelableEvent;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.httpessentials.types.Link;

import java.util.ArrayList;
import java.util.List;


/**
 * A {@link MicroFragment} that shows the event.
 *
 * @author Marten Gajda
 */
public final class ShowEventMicroFragment implements MicroFragment<EnrichedEvent>
{
    public final static Creator<ShowEventMicroFragment> CREATOR = new Creator<ShowEventMicroFragment>()
    {
        @Override
        public ShowEventMicroFragment createFromParcel(Parcel source)
        {
            ClassLoader loader = getClass().getClassLoader();
            Event event = source.readParcelable(loader);
            final int linkCount = source.readInt();
            List<Link> actionLinks = new ArrayList<>(linkCount);
            for (int i = 0; i < linkCount; ++i)
            {
                Link link = source.readParcelable(loader);
                actionLinks.add(link);
            }

            return new ShowEventMicroFragment(event, actionLinks);
        }


        @Override
        public ShowEventMicroFragment[] newArray(int size)
        {
            return new ShowEventMicroFragment[size];
        }
    };
    private final Event mEvent;
    private final List<Link> mActionLinks;


    public ShowEventMicroFragment(Event event, List<Link> actionLinks)
    {
        mEvent = event;
        mActionLinks = actionLinks;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        return mEvent.title();
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, MicroFragmentHost host)
    {
        return new EventDetailFragment();
    }


    @NonNull
    @Override
    public EnrichedEvent parameter()
    {
        return new BasicEnrichedEvent(mEvent, mActionLinks);
    }


    @Override
    public boolean skipOnBack()
    {
        return false;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mEvent instanceof Parcelable ? (Parcelable) mEvent : new ParcelableEvent(mEvent), flags);
        dest.writeInt(mActionLinks.size());
        for (Link link : mActionLinks)
        {
            dest.writeParcelable(link instanceof Parcelable ? (Parcelable) link : new ParcelableLink(link), flags);
        }
    }

}
