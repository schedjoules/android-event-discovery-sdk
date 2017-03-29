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

package com.schedjoules.eventdiscovery.framework.eventlist;

import android.support.test.espresso.IdlingPolicies;
import android.support.test.filters.Suppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.schedjoules.eventdiscovery.discovery.BasicEventDiscovery;
import com.schedjoules.eventdiscovery.testutils.activities.LauncherTestActivity;
import com.schedjoules.eventdiscovery.testutils.mockapi.MockApi;
import com.schedjoules.eventdiscovery.testutils.mockapi.MockPagesSetupBuilder;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static com.schedjoules.eventdiscovery.testutils.Durations.days;
import static com.schedjoules.eventdiscovery.testutils.Durations.hours;
import static com.schedjoules.eventdiscovery.testutils.espresso.TestUtils.waitForMinutes;
import static com.schedjoules.eventdiscovery.testutils.mockapi.pages.MockApiPages.empty;
import static com.schedjoules.eventdiscovery.testutils.mockapi.pages.MockApiPages.error;
import static com.schedjoules.eventdiscovery.testutils.mockapi.pages.MockApiPages.success;
import static java.util.Arrays.asList;


/**
 * UI test cases for Event list screen to be checked manually.
 *
 * @author Gabor Keszthelyi
 */
@Suppress // Comment this annotation out to run the manual testing
@RunWith(AndroidJUnit4.class)
public final class EventListManualAndroidTest
{
    @Rule
    public ActivityTestRule<LauncherTestActivity> mActivityTestRule =
            new ActivityTestRule<>(LauncherTestActivity.class);


    @Before
    public void setup()
    {
        IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.MINUTES);
        IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.MINUTES);
        MockApi.registerIdlingResource();
    }


    @Test
    public void testToday()
    {
        MockApi.useMockSetup(new MockPagesSetupBuilder()
                .setFirstEventTime(DateTime.now())
                .setIntervalBetweenEvents(hours(5))
                .setEventsOnPagesTop(
                        asList(
                                empty()))
                .setEventsOnPagesBottom(
                        asList(
                                success(3), success(5), success(5), success(5), error(), error(), error(), success(3), empty()))
                .setPageLoadTime(1000)
                .createMockPagesSetup());

        new BasicEventDiscovery().start(mActivityTestRule.getActivity());

        waitForMinutes(5);
    }


    @Test
    public void testVarious()
    {
        MockApi.useMockSetup(new MockPagesSetupBuilder()
                .setFirstEventTime(DateTime.now().addDuration(new Duration(1, 30, 0)))
                .setIntervalBetweenEvents(hours(5))
                .setEventsOnPagesTop(
                        asList(
                                success(5), error(), error(), success(2), success(4), success(4), error(), empty()))
                .setEventsOnPagesBottom(
                        asList(
                                success(5), success(3), success(5), success(5), error(), error(), error(), success(3), empty()))
                .setPageLoadTime(3000)
                .createMockPagesSetup());

        new BasicEventDiscovery().start(mActivityTestRule.getActivity());

        waitForMinutes(5);
    }


    @Test
    public void test_mergingItemsOnTop()
    {
        MockApi.useMockSetup(new MockPagesSetupBuilder()
                .setFirstEventTime(DateTime.now().addDuration(days(10)))
                .setIntervalBetweenEvents(hours(5))
                .setEventsOnPagesTop(
                        asList(
                                success(3), success(3), success(3), success(3), success(3), success(3), empty()))
                .setEventsOnPagesBottom(
                        asList(
                                success(5), empty()))
                .setPageLoadTime(2000)
                .createMockPagesSetup());

        new BasicEventDiscovery().start(mActivityTestRule.getActivity());

        waitForMinutes(5);
    }


    @Test
    public void test_mergingItemsOnBottom()
    {
        MockApi.useMockSetup(new MockPagesSetupBuilder()
                .setFirstEventTime(DateTime.now().addDuration(days(10)))
                .setIntervalBetweenEvents(hours(2))
                .setEventsOnPagesTop(
                        asList(
                                empty()))
                .setEventsOnPagesBottom(
                        asList(
                                success(2), success(6), success(6), success(6), success(6), empty()))
                .setPageLoadTime(2000)
                .createMockPagesSetup());

        new BasicEventDiscovery().start(mActivityTestRule.getActivity());

        waitForMinutes(5);
    }

}