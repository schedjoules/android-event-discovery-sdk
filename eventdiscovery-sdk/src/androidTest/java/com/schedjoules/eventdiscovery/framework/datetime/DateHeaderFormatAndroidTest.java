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

package com.schedjoules.eventdiscovery.framework.datetime;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.framework.eventlist.items.DateHeaderItem;

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
 * <p>
 * Note: It turned out different Android versions react differently to the locale change mechanism applied in this test, that's why the conditions on the
 * versions.
 *
 * @author Gabor Keszthelyi
 */

// TODO (Low priority) Tests depend on actual year, will fail in 2018

/*
TODO This test is not fully reviewed for all Android versions and language combinations.
Quite hard to figure it out, maybe a Parametrized test would make it easier.
Even if locale is changed for the emulator successfully by the test, the format still changes based on the language set by the phone.
See [1] and [2] also below.
*/
@RunWith(AndroidJUnit4.class)
public final class DateHeaderFormatAndroidTest
{

    private Context mContext;

    /*

    [1]: The locale changing mechanism used in this test doesn't work for DateUtils on these versions.

    [2]: The locale changing mechanism used in this test doesn't work for localized string resources on these versions.

    */


    @Before
    public void setup()
    {
        mContext = InstrumentationRegistry.getContext();
    }


    private CharSequence dateHeaderText(DateTime eventStartTime)
    {
        TextView view = new TextView(mContext);
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

        switch (Build.VERSION.SDK_INT)
        {
            case 14: // not tested
            case 15: // not tested
                break;
            case 16:
                // [1]
                break;
            case 17:
//                assertEquals("Tue, 24. October", dateHeaderText(new DateTime(2017, 10 - 1, 24, 23, 44, 33)));
//                assertEquals("Wed, 18. October", dateHeaderText(new DateTime(2017, 10 - 1, 18, 0, 44, 33)));
//                assertEquals("Mon, 1. January 2018", dateHeaderText(new DateTime(2018, 0, 1, 0, 44, 33)));
//                break;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                assertEquals("Tue, October 24", dateHeaderText(new DateTime(2017, 10 - 1, 24, 23, 44, 33)));
                assertEquals("Wed, October 18", dateHeaderText(new DateTime(2017, 10 - 1, 18, 0, 44, 33)));
                assertEquals("Mon, January 1, 2018", dateHeaderText(new DateTime(2018, 0, 1, 0, 44, 33)));
        }
    }


    @Test
    public void testFormat_UTC1_US()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        setLocale(Locale.US);

        assertEquals("Today", dateHeaderText(DateTime.now()));
        assertEquals("Tomorrow", dateHeaderText(DateTime.now().addDuration(new Duration(1, 1, 0))));

        switch (Build.VERSION.SDK_INT)
        {
            case 14: // not tested
            case 15: // not tested
                break;
            case 16:
                // [1]
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                assertEquals("Wed, October 25", dateHeaderText(new DateTime(2017, 10 - 1, 24, 23, 44, 33)));
                assertEquals("Wed, October 18", dateHeaderText(new DateTime(2017, 10 - 1, 18, 0, 44, 33)));
                assertEquals("Mon, January 1, 2018", dateHeaderText(new DateTime(2018, 0, 1, 0, 44, 33)));
        }
    }


    @Test
    public void testFormat_Netherlands()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        setLocale(new Locale("nl"));

        switch (Build.VERSION.SDK_INT)
        {
            case 14: // not tested
            case 15: // not tested
                break;
            case 18: // [2]
                break;
            case 16:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                assertEquals("Vandaag", dateHeaderText(DateTime.now()));
                assertEquals("Morgen", dateHeaderText(DateTime.now().addDuration(new Duration(1, 1, 0))));
        }

        switch (Build.VERSION.SDK_INT)
        {
            case 14: // not tested
            case 15: // not tested
                break;
            case 16: // [1]
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                assertEquals("wo, oktober 25", dateHeaderText(new DateTime(2017, 10 - 1, 24, 23, 44, 33)));
                assertEquals("wo, oktober 18", dateHeaderText(new DateTime(2017, 10 - 1, 18, 0, 44, 33)));
                assertEquals("ma, januari 1, 2018", dateHeaderText(new DateTime(2018, 0, 1, 0, 44, 33)));
                break;
            case 23:
                assertEquals("wo 25 oktober", dateHeaderText(new DateTime(2017, 10 - 1, 24, 23, 44, 33)));
                assertEquals("wo 18 oktober", dateHeaderText(new DateTime(2017, 10 - 1, 18, 0, 44, 33)));
                assertEquals("ma 1 januari 2018", dateHeaderText(new DateTime(2018, 0, 1, 0, 44, 33)));
                break;
        }
    }


    @Test
    public void testFormat_Germany()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
        setLocale(Locale.GERMAN);

        switch (Build.VERSION.SDK_INT)
        {
            case 14: // not tested
            case 15: // not tested
                break;
            case 18:
                // [2]
                break;
            case 16:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                assertEquals("Heute", dateHeaderText(DateTime.now()));
                assertEquals("Morgen", dateHeaderText(DateTime.now().addDuration(new Duration(1, 1, 0))));
        }

        switch (Build.VERSION.SDK_INT)
        {
            case 14: // not tested
            case 15: // not tested
                break;
            case 16:
                // The locale changing mechanism used in this test doesn't work for DateUtils on these versions.
                break;
            case 17:
                assertEquals("Mi., Oktober 25", dateHeaderText(new DateTime(2017, 10 - 1, 24, 23, 44, 33)));
                assertEquals("Mi., Oktober 18", dateHeaderText(new DateTime(2017, 10 - 1, 18, 0, 44, 33)));
                assertEquals("Mo., Januar 1, 2018", dateHeaderText(new DateTime(2018, 0, 1, 0, 44, 33)));
                break;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                assertEquals("Mi., 25. Oktober", dateHeaderText(new DateTime(2017, 10 - 1, 24, 23, 44, 33)));
                assertEquals("Mi., 18. Oktober", dateHeaderText(new DateTime(2017, 10 - 1, 18, 0, 44, 33)));
                assertEquals("Mo., 1. Januar 2018", dateHeaderText(new DateTime(2018, 0, 1, 0, 44, 33)));

        }

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