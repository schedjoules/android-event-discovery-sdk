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

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;
import java.util.TimeZone;

import static com.schedjoules.eventdiscovery.utils.DateTimeFormatter.smartDayFormat;
import static junit.framework.Assert.assertEquals;


/**
 * Test for {@link DateTimeFormatter}.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(AndroidJUnit4.class)
public class DateTimeFormatterAndroidTest
{

    private Context mContext;


    @Before
    public void setup()
    {
        mContext = InstrumentationRegistry.getContext();
    }


    @Test
    public void testSmartDayFormat_UTC_US()
    {
        TimeZone.setDefault(DateTime.UTC);
        setLocale(Locale.US);

        DateTime now = DateTime.now();

        assertEquals("Today", smartDayFormat(mContext, now));

        assertEquals("Tomorrow", smartDayFormat(mContext, now.addDuration(new Duration(1, 1, 0))));

        assertEquals("Mon, Oct 24", smartDayFormat(mContext, new DateTime(2016, 10 - 1, 24, 23, 44, 33)));

        assertEquals("Tue, Oct 18", smartDayFormat(mContext, new DateTime(2016, 10 - 1, 18, 0, 44, 33)));

        assertEquals("Sun, Jan 1, 2017", smartDayFormat(mContext, new DateTime(2017, 0, 1, 0, 44, 33)));
    }


    @Test
    public void testSmartDayFormat_UTC1_US()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        setLocale(Locale.US);

        assertEquals("Today", smartDayFormat(mContext, DateTime.now()));

        assertEquals("Tomorrow", smartDayFormat(mContext, DateTime.now().addDuration(new Duration(1, 1, 0))));

        assertEquals("Tue, Oct 25", smartDayFormat(mContext, new DateTime(2016, 10 - 1, 24, 23, 44, 33)));

        assertEquals("Tue, Oct 18", smartDayFormat(mContext, new DateTime(2016, 10 - 1, 18, 0, 44, 33)));

        assertEquals("Sun, Jan 1, 2017", smartDayFormat(mContext, new DateTime(2017, 0, 1, 0, 44, 33)));
    }


    @Test
    public void testSmartDayFormat_Netherlands()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        setLocale(new Locale("nl"));

        assertEquals("Vandaag", smartDayFormat(mContext, DateTime.now()));

        assertEquals("Morgen", smartDayFormat(mContext, DateTime.now().addDuration(new Duration(1, 1, 0))));

        assertEquals("di 25 okt.", smartDayFormat(mContext, new DateTime(2016, 10 - 1, 24, 23, 44, 33)));

        assertEquals("di 18 okt.", smartDayFormat(mContext, new DateTime(2016, 10 - 1, 18, 0, 44, 33)));

        assertEquals("zo 1 jan. 2017", smartDayFormat(mContext, new DateTime(2017, 0, 1, 0, 44, 33)));
    }


    @Test
    public void testSmartDayFormat_Germany()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
        setLocale(Locale.GERMAN);

        assertEquals("Heute", smartDayFormat(mContext, DateTime.now()));

        assertEquals("Morgen", smartDayFormat(mContext, DateTime.now().addDuration(new Duration(1, 1, 0))));

        assertEquals("Di., 25. Okt.", smartDayFormat(mContext, new DateTime(2016, 10 - 1, 24, 23, 44, 33)));

        assertEquals("Di., 18. Okt.", smartDayFormat(mContext, new DateTime(2016, 10 - 1, 18, 0, 44, 33)));

        assertEquals("So., 1. Jan. 2017", smartDayFormat(mContext, new DateTime(2017, 0, 1, 0, 44, 33)));
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