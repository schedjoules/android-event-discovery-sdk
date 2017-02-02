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
 * Configuration for {@link MockResultPages}.
 *
 * @author Gabor Keszthelyi
 */
public final class MockPagesSetup
{
    /*
     * Note: The Builder ({@link MockPagesSetupBuilder}) can be generated
     * with 'Replace constructor with builder' IntelliJ action.
     */

    public final DateTime mFirstEventTime;
    public final Duration mIntervalBetweenEvents;
    public final int mPageLoadTime;
    public final List<MockApiResultPage> mEventsOnPagesBottom;
    public final List<MockApiResultPage> mEventsOnPagesTop;


    public MockPagesSetup(DateTime firstEventTime, Duration intervalBetweenEvents, int pageLoadTime, List<MockApiResultPage> eventsOnPagesBottom, List<MockApiResultPage> eventsOnPagesTop)
    {
        mFirstEventTime = firstEventTime;
        mIntervalBetweenEvents = intervalBetweenEvents;
        mPageLoadTime = pageLoadTime;
        mEventsOnPagesBottom = eventsOnPagesBottom;
        mEventsOnPagesTop = eventsOnPagesTop;
    }
}
