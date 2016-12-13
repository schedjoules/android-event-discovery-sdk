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

package com.schedjoules.eventdiscovery.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.schedjoules.eventdiscovery.eventlist.items.DateHeaderItem;
import com.schedjoules.eventdiscovery.eventlist.items.DateHeaderItemView;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;


/**
 * Test for the displayed date format of {@link DateHeaderItem}.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(AndroidJUnit4.class)
public class DateHeaderFormatAndroidTest
{

    private Context mContext;


    @Before
    public void setup()
    {
        mContext = InstrumentationRegistry.getContext();
    }


    private CharSequence dateHeaderText(DateTime eventStartTime)
    {
        DateHeaderItemView view = new DateHeaderItemView(mContext, null);
        new DateHeaderItem(eventStartTime).bindDataTo(view);
        return view.getText();
    }


    @Test
    public void testFormat_UTC_US()
    {
        TimeZone.setDefault(DateTime.UTC);
        setLocale(Locale.US);

        assertEquals("Today", dateHeaderText(DateTime.now()));
        assertEquals("Tomorrow", dateHeaderText(DateTime.now().addDuration(new Duration(1, 1, 0))));
        assertEquals("Mon, Oct 24", dateHeaderText(new DateTime(2016, 10 - 1, 24, 23, 44, 33)));
        assertEquals("Tue, Oct 18", dateHeaderText(new DateTime(2016, 10 - 1, 18, 0, 44, 33)));
        assertEquals("Sun, Jan 1, 2017", dateHeaderText(new DateTime(2017, 0, 1, 0, 44, 33)));
    }


    @Test
    public void testFormat_UTC1_US()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        setLocale(Locale.US);

        assertEquals("Today", dateHeaderText(DateTime.now()));
        assertEquals("Tomorrow", dateHeaderText(DateTime.now().addDuration(new Duration(1, 1, 0))));
        assertEquals("Tue, Oct 25", dateHeaderText(new DateTime(2016, 10 - 1, 24, 23, 44, 33)));
        assertEquals("Tue, Oct 18", dateHeaderText(new DateTime(2016, 10 - 1, 18, 0, 44, 33)));
        assertEquals("Sun, Jan 1, 2017", dateHeaderText(new DateTime(2017, 0, 1, 0, 44, 33)));
    }


    @Test
    public void testFormat_UTC1_USooo()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        setLocale(Locale.US);

        DateHeaderItemView view = new DateHeaderItemView(mContext, null);
        new DateHeaderItem(DateTime.now()).bindDataTo(view);
        CharSequence text = view.getText();
        assertEquals("Today", text);

        assertEquals("Today", dateHeaderText(DateTime.now()));
        assertEquals("Tomorrow", dateHeaderText(DateTime.now().addDuration(new Duration(1, 1, 0))));
        assertEquals("Tue, Oct 25", dateHeaderText(new DateTime(2016, 10 - 1, 24, 23, 44, 33)));
        assertEquals("Tue, Oct 18", dateHeaderText(new DateTime(2016, 10 - 1, 18, 0, 44, 33)));
        assertEquals("Sun, Jan 1, 2017", dateHeaderText(new DateTime(2017, 0, 1, 0, 44, 33)));
    }


    @Test
    public void testFormat_Netherlands()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        setLocale(new Locale("nl"));

        assertEquals("Vandaag", dateHeaderText(DateTime.now()));
        assertEquals("Morgen", dateHeaderText(DateTime.now().addDuration(new Duration(1, 1, 0))));
        assertEquals("di 25 okt.", dateHeaderText(new DateTime(2016, 10 - 1, 24, 23, 44, 33)));
        assertEquals("di 18 okt.", dateHeaderText(new DateTime(2016, 10 - 1, 18, 0, 44, 33)));
        assertEquals("zo 1 jan. 2017", dateHeaderText(new DateTime(2017, 0, 1, 0, 44, 33)));
    }


    @Test
    public void testFormat_Germany()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
        setLocale(Locale.GERMAN);

        assertEquals("Heute", dateHeaderText(DateTime.now()));
        assertEquals("Morgen", dateHeaderText(DateTime.now().addDuration(new Duration(1, 1, 0))));
        assertEquals("Di., 25. Okt.", dateHeaderText(new DateTime(2016, 10 - 1, 24, 23, 44, 33)));
        assertEquals("Di., 18. Okt.", dateHeaderText(new DateTime(2016, 10 - 1, 18, 0, 44, 33)));
        assertEquals("So., 1. Jan. 2017", dateHeaderText(new DateTime(2017, 0, 1, 0, 44, 33)));
    }


    @Test
    public void testThatTodayRespectsLocalTime()
    {
        setLocale(Locale.US);

        // One of these two time zones must fall on a different local day than in UTC,
        // so if the involved DateUtils.isToday() call would use UTC time, the test would fail.

        TimeZone.setDefault(TimeZone.getTimeZone("GMT-12"));
        assertEquals("Today", dateHeaderText(DateTime.now()));

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+14"));
        assertEquals("Today", dateHeaderText(DateTime.now()));
    }


    @Test
    public void testThatTodayWorksWithPastHours()
    {
        TimeZone.setDefault(DateTime.UTC);
        setLocale(Locale.US);

        DateTime now = DateTime.now();
        DateTime tenMinutesAgo = now.addDuration(new Duration(-1, 0, 0, 10, 0));
        if (now.toAllDay().equals(tenMinutesAgo.toAllDay()))
        {
            assertEquals("Today", dateHeaderText(tenMinutesAgo));
        }
    }


    // http://www.andreamaglie.com/a-test-rule-for-setting-device-locale/
    private void setLocale(Locale locale)
    {
        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        Locale.setDefault(locale);
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

}