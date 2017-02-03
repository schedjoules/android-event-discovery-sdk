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

package com.schedjoules.eventdiscovery.testutils.mockapi;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.idling.CountingIdlingResource;

import com.schedjoules.client.Api;
import com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection;
import com.schedjoules.eventdiscovery.testutils.DummyResultPage;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URI;


/**
 * Mocking capable implementation of {@link Api}.
 *
 * @author Gabor Keszthelyi
 */
public final class MockApi implements Api
{
    private static MockResultPages sMockResultPages;

    private static CountingIdlingResource sIdlingResource = new CountingIdlingResource("MockApi");


    public static void useMockSetup(MockPagesSetup mockPagesSetup)
    {
        sMockResultPages = new MockResultPages(mockPagesSetup);
    }


    public static void registerIdlingResource()
    {
        Espresso.registerIdlingResources(sIdlingResource);
    }


    @Override
    public <T> T queryResult(URI uri, HttpRequest<T> httpRequest) throws ProtocolException, ProtocolError, IOException
    {
        try
        {
            sIdlingResource.increment();
            if (uri.toString().contains("/events?start_at_or_after="))
            {
                return (T) sMockResultPages.nextResultPage(ScrollDirection.BOTTOM);
            }
            if (uri.equals(DummyResultPage.URI_TOP))
            {
                return (T) sMockResultPages.nextResultPage(ScrollDirection.TOP);
            }
            else if (uri.equals(DummyResultPage.URI_BOTTOM))
            {
                return (T) sMockResultPages.nextResultPage(ScrollDirection.BOTTOM);
            }
            return null;
        }
        finally
        {
            sIdlingResource.decrement();
        }
    }

}

