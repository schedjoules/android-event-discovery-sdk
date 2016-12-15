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

package com.schedjoules.eventdiscovery.eventdetails.wizardsteps;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.eventdetails.EventDetailFragment;
import com.schedjoules.eventdiscovery.model.ParcelableEvent;
import com.schedjoules.eventdiscovery.model.ParcelableLink;

import org.dmfs.android.dumbledore.WizardStep;
import org.dmfs.httpessentials.types.Link;

import java.util.ArrayList;
import java.util.List;


/**
 * A {@link WizardStep} that shows the event.
 *
 * @author Marten Gajda
 */
public final class ShowEventStep implements WizardStep
{
    private final Event mEvent;
    private final List<Link> mActionLinks;


    public ShowEventStep(Event event, List<Link> actionLinks)
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
    public Fragment fragment(@NonNull Context context)
    {
        return EventDetailFragment.newInstance(mEvent, mActionLinks);
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


    public final static Creator<ShowEventStep> CREATOR = new Creator<ShowEventStep>()
    {
        @Override
        public ShowEventStep createFromParcel(Parcel source)
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

            return new ShowEventStep(event, actionLinks);
        }


        @Override
        public ShowEventStep[] newArray(int size)
        {
            return new ShowEventStep[size];
        }
    };
}
