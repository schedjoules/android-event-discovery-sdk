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

package com.schedjoules.eventdiscovery.framework.utils;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.format.DateUtils;

import com.schedjoules.eventdiscovery.framework.datetime.SmartFormattedDay;

import org.dmfs.rfc5545.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
import static android.text.format.DateUtils.FORMAT_ABBREV_WEEKDAY;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;


/**
 * A test that shows various {@link DateUtils} formatted texts on the screen to be able to check and compare them
 * easily.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(AndroidJUnit4.class)
public final class DateUtilsVariationsChecker
{

    private static final String NL = "\n";

    @Rule
    public ActivityTestRule<TestMessageActivity> mActivityTestRule = new ActivityTestRule<>(TestMessageActivity.class);

    private Context mContext;


    @Before
    public void setup()
    {
        mContext = getContext();
    }


    @Test
    public void testDateUtils() throws InterruptedException
    {
        mActivityTestRule.launchActivity(
                TestMessageActivity.launch(mActivityTestRule.getActivity(), variousDateUtilsFormats()));

        TestMessageActivity.waitUntilNotified();

    }


    private String variousDateUtilsFormats()
    {
        StringBuilder result = new StringBuilder();

        result.append("TimeZone: ").append(TimeZone.getDefault()).append(NL);
        result.append("Locale: ")
                .append(Locale.getDefault())
                .append(NL)
                .append("--------------------------")
                .append(NL)
                .append(NL);

        Map<String, Long> dates = new LinkedHashMap<>();
        dates.put("Now", System.currentTimeMillis());
        dates.put("1 day from now", System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS);
        dates.put("2 days from now", System.currentTimeMillis() + 2 * DateUtils.DAY_IN_MILLIS);
        dates.put("3 days from now", System.currentTimeMillis() + 3 * DateUtils.DAY_IN_MILLIS);
        dates.put("7 days from now", System.currentTimeMillis() + 7 * DateUtils.DAY_IN_MILLIS);
        dates.put("8 days from now", System.currentTimeMillis() + 8 * DateUtils.DAY_IN_MILLIS);
        dates.put("32 days from now", System.currentTimeMillis() + 32 * DateUtils.DAY_IN_MILLIS);
        dates.put("90 days from now", System.currentTimeMillis() + 90 * DateUtils.DAY_IN_MILLIS);
        dates.put("1 year from now", System.currentTimeMillis() + DateUtils.YEAR_IN_MILLIS);

        List<Formatter> formatters = new ArrayList<>();

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "SmartFormattedDay";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return new SmartFormattedDay(new DateTime(timestamp)).value(mContext);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "formatDateTime FORMAT_SHOW_DATE | FORMAT_SHOW_WEEKDAY";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.formatDateTime(mContext, timestamp, FORMAT_SHOW_DATE | FORMAT_SHOW_WEEKDAY);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "formatDateTime FORMAT_SHOW_DATE | FORMAT_SHOW_WEEKDAY | FORMAT_ABBREV_MONTH";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.formatDateTime(mContext, timestamp,
                        FORMAT_SHOW_DATE | FORMAT_SHOW_WEEKDAY | FORMAT_ABBREV_MONTH);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "formatDateTime FORMAT_SHOW_DATE | FORMAT_SHOW_WEEKDAY | FORMAT_ABBREV_MONTH | FORMAT_ABBREV_WEEKDAY";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.formatDateTime(mContext, timestamp,
                        FORMAT_SHOW_DATE | FORMAT_SHOW_WEEKDAY | FORMAT_ABBREV_MONTH | FORMAT_ABBREV_WEEKDAY);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeDateTimeString FORMAT_SHOW_WEEKDAY FORMAT_SHOW_DATE";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeDateTimeString(
                        mContext, timestamp, DAY_IN_MILLIS, DAY_IN_MILLIS,
                        FORMAT_SHOW_WEEKDAY | FORMAT_SHOW_DATE);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeTimeSpanString(timestamp)";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeTimeSpanString(timestamp);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeTimeSpanString(context, timestamp)";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeTimeSpanString(mContext, timestamp);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeTimeSpanString(timestamp,now,minResolution=DAY_IN_MILLIS)";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), DAY_IN_MILLIS);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeTimeSpanString(timestamp,now,minResolution=DAY_IN_MILLIS)";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), DAY_IN_MILLIS);
            }
        });

        printDates(result, dates, formatters);

        return result.toString();
    }


    private void printDates(StringBuilder sb, Map<String, Long> dates, List<Formatter> formatters)
    {
        for (int i = 0; i < formatters.size(); i++)
        {
            sb.append("#").append(i + 1).append(" ").append(formatters.get(i).name()).append(NL).append(NL);

            for (Map.Entry<String, Long> date : dates.entrySet())
            {
                sb.append(formatters.get(i).format(date.getValue())).append(NL);
//                        .append("  <- ").append(date.getKey()).append(NL);
            }
            sb.append(NL).append("----------------------------").append(NL).append(NL);
        }
    }


    private interface Formatter
    {
        String name();

        CharSequence format(Long timestamp);
    }

}
