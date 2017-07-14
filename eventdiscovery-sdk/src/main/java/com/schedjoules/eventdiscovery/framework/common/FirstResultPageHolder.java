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

package com.schedjoules.eventdiscovery.framework.common;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.schedjoules.client.eventsdiscovery.Envelope;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.ResultPage;

import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;


/**
 * Holds the first result page loaded on splash screen in memory to be able to access it from the list screen.
 * <p>
 * Note: This is workaround that may live for a while but could potentially be removed later if caching is worked out.
 *
 * @author Gabor Keszthelyi
 */
public final class FirstResultPageHolder extends ViewModel
{

    /*
    TODO Improve the design of this class if needed.
    Note: The same immutable design as `CategoriesCache` doesn't work here if we want to clear the value.
     */

    private Optional<ResultPage<Envelope<Event>>> mFirstResultPage = new Absent<>();

    public static void set(ResultPage<Envelope<Event>> firstResultPage, FragmentActivity activity)
    {
        ViewModelProviders.of(activity).get(FirstResultPageHolder.class).mFirstResultPage = new Present<>(firstResultPage);
    }


    public static Optional<ResultPage<Envelope<Event>>> get(FragmentActivity activity)
    {
        return ViewModelProviders.of(activity).get(FirstResultPageHolder.class).mFirstResultPage;
    }


    public static void clear(FragmentActivity activity)
    {
        ViewModelProviders.of(activity).get(FirstResultPageHolder.class).mFirstResultPage = new Absent<>();
    }
}
