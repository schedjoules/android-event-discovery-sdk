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
import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;
import com.schedjoules.eventdiscovery.framework.model.StructuredResultPage;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BoxFactory;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;

import org.dmfs.optional.Optional;


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
        dest.writeParcelable(new IterableBox<>(mResultPage, EventEnvelopeBox.FACTORY), flags);

        BoxFactory<ApiQuery<ResultPage<Envelope<Event>>>> queryBoxFactory = new ApiQueryBox.Factory<>();
        dest.writeParcelable(new OptionalBox<>(mResultPage.previousPageQuery(), queryBoxFactory), flags);
        dest.writeParcelable(new OptionalBox<>(mResultPage.nextPageQuery(), queryBoxFactory), flags);
    }


    public static final Creator<EventResultPageBox> CREATOR = new Creator<EventResultPageBox>()
    {
        @Override
        public EventResultPageBox createFromParcel(Parcel in)
        {
            Box<Iterable> iterableBox = in.readParcelable(getClass().getClassLoader());

            Box<Optional<ApiQuery<ResultPage>>> prevQueryBox = in.readParcelable(getClass().getClassLoader());
            Box<Optional<ApiQuery<ResultPage>>> nextQueryBox = in.readParcelable(getClass().getClassLoader());

            return new EventResultPageBox(new StructuredResultPage(iterableBox.content(), prevQueryBox.content(), nextQueryBox.content()));
        }


        @Override
        public EventResultPageBox[] newArray(int size)
        {
            return new EventResultPageBox[size];
        }
    };
}
