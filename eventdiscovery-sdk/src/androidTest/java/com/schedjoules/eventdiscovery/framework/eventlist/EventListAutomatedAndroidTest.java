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
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.schedjoules.eventdiscovery.R;
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

import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.schedjoules.eventdiscovery.testutils.espresso.TestUtils.toLandscape;
import static com.schedjoules.eventdiscovery.testutils.espresso.TestUtils.toPortrait;
import static com.schedjoules.eventdiscovery.testutils.espresso.TestUtils.waitForMillis;
import static com.schedjoules.eventdiscovery.testutils.mockapi.pages.MockApiPages.empty;
import static com.schedjoules.eventdiscovery.testutils.mockapi.pages.MockApiPages.error;
import static com.schedjoules.eventdiscovery.testutils.mockapi.pages.MockApiPages.success;
import static java.util.Arrays.asList;


/**
 * Automated UI test cases for Event list screen.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(AndroidJUnit4.class)
public class EventListAutomatedAndroidTest
{
    // Set to other than zero if you want to watch the test
    private static int WATCH_BREAK_MILLIS = 0;

    private static MockPagesSetupBuilder DEFAULT_PAGES_SETUP_BUILDER = new MockPagesSetupBuilder()
            .setFirstEventTime(DateTime.now().addDuration(new Duration(1, 5, 0)))
            .setIntervalBetweenEvents(new Duration(1, 0, 5, 0, 0))
            .setEventsOnPagesBottom(
                    asList(success(3), success(3), success(3), error(), error(), error(), error(), success(3), empty()))
            .setEventsOnPagesTop(
                    asList(success(3), success(3), success(3), error(), success(3), success(3), success(3), success(3),
                            empty()))
            .setPageLoadTime(2000);

    @Rule
    public ActivityTestRule<LauncherTestActivity> mActivityTestRule =
            new ActivityTestRule<>(LauncherTestActivity.class);


    @Before
    public void setup()
    {
        IdlingPolicies.setIdlingResourceTimeout(155, TimeUnit.SECONDS);
        IdlingPolicies.setMasterPolicyTimeout(155, TimeUnit.SECONDS);
        MockApi.registerIdlingResource();
    }


    @Test
    public void test_errorBgMessageWhenFirstRequestFails_keepsItOnRotation()
    {
        toPortrait();

        MockApi.useMockSetup(DEFAULT_PAGES_SETUP_BUILDER
                .setEventsOnPagesBottom(asList(error(), error(), error(), error(), error(), error(), error(), empty()))
                .createMockPagesSetup());

        new BasicEventDiscovery().start(mActivityTestRule.getActivity());

        onView(withText(R.string.schedjoules_event_list_error_bg_msg)).check(matches(isDisplayed()));
        watchBreak();

        toLandscape();

        onView(withText(R.string.schedjoules_event_list_error_bg_msg)).check(matches(isDisplayed()));
        watchBreak();
    }


    @Test
    public void test_noEventsFoundBgMessageWhenFirstRequestEmpty_keepsItOnRotation()
    {
        toPortrait();

        MockApi.useMockSetup(DEFAULT_PAGES_SETUP_BUILDER
                .setEventsOnPagesBottom(asList(empty()))
                .setEventsOnPagesTop(asList(empty()))
                .createMockPagesSetup());

        new BasicEventDiscovery().start(mActivityTestRule.getActivity());

        onView(withText(R.string.schedjoules_event_list_no_events_found)).check(matches(isDisplayed()));
        watchBreak();

        toLandscape();

        onView(withText(R.string.schedjoules_event_list_no_events_found)).check(matches(isDisplayed()));
        watchBreak();
    }


    /**
     * For verifying against this bug: https://github.com/schedjoules/android-event-discovery-sdk/issues/23
     */
    @Test
    public void test_localTimeDoesNotMessUpTodayTomorrow()
    {
        TimeZone originalTimeZone = TimeZone.getDefault();
        Locale originalLocale = Locale.getDefault();

        // ARRANGE
        DateTime now = DateTime.now();
        DateTime startTime = new DateTime(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 0);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        Locale.setDefault(Locale.US);

        MockApi.useMockSetup(new MockPagesSetupBuilder()
                .setFirstEventTime(startTime)
                .setIntervalBetweenEvents(new Duration(1, 0, 0, 40, 0))
                .setEventsOnPagesBottom(asList(success(4), empty()))
                .setEventsOnPagesTop(asList(empty()))
                .setPageLoadTime(1000)
                .createMockPagesSetup());

        // ACT
        new BasicEventDiscovery().start(mActivityTestRule.getActivity());

        // ASSERT
        onView(withText("Today")).check(doesNotExist());
        onView(withText("Tomorrow")).check(matches(isDisplayed()));
        watchBreak();

        // reset locale and timezone
        Locale.setDefault(originalLocale);
        TimeZone.setDefault(originalTimeZone);
    }


    private void watchBreak()
    {
        if (WATCH_BREAK_MILLIS != 0)
        {
            waitForMillis(WATCH_BREAK_MILLIS);
        }
    }

}