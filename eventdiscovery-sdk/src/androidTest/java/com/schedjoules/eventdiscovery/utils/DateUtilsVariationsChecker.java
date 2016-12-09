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
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.format.DateUtils;

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
import static android.text.format.DateUtils.FORMAT_ABBREV_ALL;
import static android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
import static android.text.format.DateUtils.FORMAT_ABBREV_TIME;
import static android.text.format.DateUtils.FORMAT_ABBREV_WEEKDAY;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;


/**
 * A test that shows various {@link DateUtils} formatted texts on the screen to be able to check and compare them easily.
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

        dates.put("1 day before", System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS);
        dates.put("2 days before", System.currentTimeMillis() - 2 * DateUtils.DAY_IN_MILLIS);
        dates.put("3 days before", System.currentTimeMillis() - 3 * DateUtils.DAY_IN_MILLIS);
        dates.put("7 days before", System.currentTimeMillis() - 7 * DateUtils.DAY_IN_MILLIS);
        dates.put("8 days before", System.currentTimeMillis() - 8 - DateUtils.DAY_IN_MILLIS);
        dates.put("1 year before", System.currentTimeMillis() - DateUtils.WEEK_IN_MILLIS);

        // 1481326200000  --  Fri, 09 Dec 2016 23:30:00 UTC  --  12/10/2016, 12:30:00 AM UTC+1
        dates.put("Fri, 09 Dec 2016 23:30:00 UTC", 1481326200000L);

        // 1478133000000   --  Thu, 03 Nov 2016 00:30:00 UTC  --  11/3/2016, 1:30:00 AM UTC+1
        dates.put("Thu, 03 Nov 2016 00:30:00 UTC", 1478133000000L);

        List<Formatter> formatters = new ArrayList<>();

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "double";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                String date = DateUtils.formatDateTime(getContext(), timestamp, FORMAT_SHOW_DATE);
                String day = DateUtils.formatDateTime(getContext(), timestamp, FORMAT_SHOW_WEEKDAY);
                if (DateUtils.isToday(timestamp))
                {
                    return "Today                                   " + date;
                }
                else if (DateUtils.isToday(timestamp - DAY_IN_MILLIS))
                {
                    return "Tomorrow                                " + date;
                }
                else
                {
                    return day + "                                  "  + date;
                }
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "special 1";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                String cs = DateUtils.formatDateTime(getContext(), timestamp,
                        FORMAT_SHOW_WEEKDAY | FORMAT_SHOW_DATE | FORMAT_ABBREV_ALL);
                if (DateUtils.isToday(timestamp))
                {
                    return String.format("Today (%s)", cs);
                }
                else if (DateUtils.isToday(timestamp - DAY_IN_MILLIS))
                {
                    return String.format("Tomorrow (%s)", cs);
                }
                else
                {
                    return cs;
                }
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeDateTimeString DAY_IN_MILLIS DAY_IN_MILLIS 0";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeDateTimeString(mContext, timestamp, DateUtils.DAY_IN_MILLIS,
                        DateUtils.DAY_IN_MILLIS, 0);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeDateTimeString DAY_IN_MILLIS DAY_IN_MILLIS FORMAT_SHOW_WEEKDAY";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeDateTimeString(
                        mContext, timestamp, DAY_IN_MILLIS, DAY_IN_MILLIS,
                        FORMAT_SHOW_WEEKDAY);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeDateTimeString DAY_IN_MILLIS DAY_IN_MILLIS FORMAT_SHOW_WEEKDAY | FORMAT_ABBREV_TIME";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeDateTimeString(
                        mContext, timestamp, DAY_IN_MILLIS, DAY_IN_MILLIS,
                        FORMAT_SHOW_WEEKDAY | FORMAT_ABBREV_TIME);
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
                return "formatDateTime FORMAT_ABBREV_MONTH | FORMAT_ABBREV_WEEKDAY";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.formatDateTime(mContext, timestamp,
                        FORMAT_ABBREV_MONTH | FORMAT_ABBREV_WEEKDAY);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "formatDateTime FORMAT_SHOW_WEEKDAY | FORMAT_SHOW_DATE | FORMAT_ABBREV_ALL";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.formatDateTime(mContext, timestamp,
                        FORMAT_SHOW_WEEKDAY | FORMAT_SHOW_DATE | FORMAT_ABBREV_ALL);
            }
        });

        formatters.add(new Formatter()
        {
            @Override
            public String name()
            {
                return "getRelativeTimeSpanString(millis)";
            }


            @Override
            public CharSequence format(Long timestamp)
            {
                return DateUtils.getRelativeTimeSpanString(getContext(), timestamp);
            }
        });

        // TODO
//        formatters.add(new Formatter()
//        {
//            @Override
//            public String name()
//            {
//                return "getRelativeTimeSpanString(time,now,minResolution) ";
//            }
//
//
//            @Override
//            public CharSequence format(Long timestamp)
//            {
//                return DateUtils.getRelativeTimeSpanString(timestamp, DateUtils.DAY_IN_MILLIS, FORMAT_SHOW_WEEKDAY);
//            }
//        });

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
                sb.append(formatters.get(i).format(date.getValue())).append("  <- ").append(date.getKey()).append(NL);
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
