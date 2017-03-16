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

import com.schedjoules.eventdiscovery.testutils.mockapi.pages.MockApiResultPage;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;

import java.util.List;


/**
 * Generated with 'Replace constructor with builder' IntelliJ action for {@link MockPagesSetup}.
 */
public final class MockPagesSetupBuilder
{
    private DateTime mFirstEventTime;
    private Duration mIntervalBetweenEvents;
    private int mPageLoadTime;
    private List<MockApiResultPage> mEventsOnPagesBottom;
    private List<MockApiResultPage> mEventsOnPagesTop;


    public MockPagesSetupBuilder setFirstEventTime(DateTime firstEventTime)
    {
        mFirstEventTime = firstEventTime;
        return this;
    }


    public MockPagesSetupBuilder setIntervalBetweenEvents(Duration intervalBetweenEvents)
    {
        mIntervalBetweenEvents = intervalBetweenEvents;
        return this;
    }


    public MockPagesSetupBuilder setPageLoadTime(int pageLoadTime)
    {
        mPageLoadTime = pageLoadTime;
        return this;
    }


    public MockPagesSetupBuilder setEventsOnPagesBottom(List<MockApiResultPage> eventsOnPagesBottom)
    {
        mEventsOnPagesBottom = eventsOnPagesBottom;
        return this;
    }


    public MockPagesSetupBuilder setEventsOnPagesTop(List<MockApiResultPage> eventsOnPagesTop)
    {
        mEventsOnPagesTop = eventsOnPagesTop;
        return this;
    }


    public MockPagesSetup createMockPagesSetup()
    {
        return new MockPagesSetup(mFirstEventTime, mIntervalBetweenEvents, mPageLoadTime, mEventsOnPagesBottom,
                mEventsOnPagesTop);
    }
}