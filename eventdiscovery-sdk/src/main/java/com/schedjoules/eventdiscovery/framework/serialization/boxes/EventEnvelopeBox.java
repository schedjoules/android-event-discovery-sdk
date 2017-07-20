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

package com.schedjoules.eventdiscovery.framework.serialization.boxes;

import android.os.Parcel;

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.model.envelope.StructuredEnvelope;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BoxFactory;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;

import org.dmfs.optional.Optional;


/**
 * {@link Box} for {@link Envelope<Event>}
 *
 * @author Gabor Keszthelyi
 */
public final class EventEnvelopeBox implements Box<Envelope<Event>>
{
    private final Envelope<Event> mEnvelope;


    public EventEnvelopeBox(Envelope<Event> envelope)
    {
        mEnvelope = envelope;
    }


    @Override
    public Envelope<Event> content()
    {
        return mEnvelope;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mEnvelope.etag());
        dest.writeString(mEnvelope.uid());
        dest.writeParcelable(new OptionalBox<>(mEnvelope.payload(), EventBox.FACTORY), flags);
    }


    public static final Creator<Box<Envelope<Event>>> CREATOR = new Creator<Box<Envelope<Event>>>()
    {
        @Override
        public EventEnvelopeBox createFromParcel(Parcel in)
        {
            String etag = in.readString();
            String uid = in.readString();
            Box<Optional<Event>> optEventBox = in.readParcelable(getClass().getClassLoader());
            return new EventEnvelopeBox(new StructuredEnvelope<>(etag, uid, optEventBox.content()));
        }


        @Override
        public EventEnvelopeBox[] newArray(int size)
        {
            return new EventEnvelopeBox[size];
        }
    };

    public static final BoxFactory<Envelope<Event>> FACTORY = new BoxFactory<Envelope<Event>>()
    {
        @Override
        public Box<Envelope<Event>> create(Envelope<Event> value)
        {
            return new EventEnvelopeBox(value);
        }
    };

}
