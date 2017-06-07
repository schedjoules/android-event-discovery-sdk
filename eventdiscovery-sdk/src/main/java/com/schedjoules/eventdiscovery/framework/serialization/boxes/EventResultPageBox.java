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

import com.schedjoules.client.ApiQuery;
import com.schedjoules.client.State;
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.model.StructuredResultPage;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * {@link Box} for {@link ResultPage} with {@link Envelope} of {@link Event}.
 *
 * @author Gabor Keszthelyi
 */
public final class EventResultPageBox implements Box<ResultPage<Envelope<Event>>>
{
    private final ResultPage<Envelope<Event>> mResultPage;


    public EventResultPageBox(ResultPage<Envelope<Event>> resultPage)
    {
        mResultPage = resultPage;
    }


    @Override
    public ResultPage<Envelope<Event>> content()
    {
        return mResultPage;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        Iterable<Envelope<Event>> envelopes = mResultPage.items();
        List<Box<Envelope<Event>>> envelopeBoxes = new LinkedList<>();
        for (Envelope<Event> envelope : envelopes)
        {
            envelopeBoxes.add(new EventEnvelopeBox(envelope));
        }
        dest.writeTypedList(envelopeBoxes);

        dest.writeInt(mResultPage.isFirstPage() ? 1 : 0);
        dest.writeInt(mResultPage.isLastPage() ? 1 : 0);

        if (!mResultPage.isFirstPage())
        {
            dest.writeSerializable(mResultPage.previousPageQuery().serializable());
        }
        if (!mResultPage.isLastPage())
        {
            dest.writeSerializable(mResultPage.nextPageQuery().serializable());
        }
    }


    public static final Creator<EventResultPageBox> CREATOR = new Creator<EventResultPageBox>()
    {
        @Override
        public EventResultPageBox createFromParcel(Parcel in)
        {
            // TODO Use Mapped Iterable when available:
            List<Box<Envelope<Event>>> envelopeBoxes = new LinkedList<>();
            in.readTypedList(envelopeBoxes, EventEnvelopeBox.CREATOR);
            List<Envelope<Event>> envelopes = new ArrayList<>();
            for (Box<Envelope<Event>> envelopeBox : envelopeBoxes)
            {
                envelopes.add(envelopeBox.content());
            }

            boolean isFirstPage = in.readInt() == 1;
            boolean isLastPage = in.readInt() == 1;

            ApiQuery<ResultPage<Envelope<Event>>> prevQuery = null;
            if (!isFirstPage)
            {
                prevQuery = ((State<ApiQuery<ResultPage<Envelope<Event>>>>) in.readSerializable()).restored();
            }
            ApiQuery<ResultPage<Envelope<Event>>> nextQuery = null;
            if (!isLastPage)
            {
                nextQuery = ((State<ApiQuery<ResultPage<Envelope<Event>>>>) in.readSerializable()).restored();
            }

            return new EventResultPageBox(new StructuredResultPage(envelopes, isFirstPage, isLastPage, prevQuery, nextQuery));
        }


        @Override
        public EventResultPageBox[] newArray(int size)
        {
            return new EventResultPageBox[size];
        }
    };
}
