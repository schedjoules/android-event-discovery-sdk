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

package com.schedjoules.eventdiscovery.framework.location.model.namedplace;

/**
 * Comma separated text from {@link NamedPlace}, like "Amsterdam, Netherlands".
 *
 * @author Gabor Keszthelyi
 */
public final class CommaSeparated implements CharSequence
{
    private final NamedPlace mNamedPlace;

    private String mToString;


    public CommaSeparated(NamedPlace namedPlace)
    {
        mNamedPlace = namedPlace;
    }


    @Override
    public int length()
    {
        return toString().length();
    }


    @Override
    public char charAt(int index)
    {
        return toString().charAt(index);
    }


    @Override
    public CharSequence subSequence(int start, int end)
    {
        return toString().subSequence(start, end);
    }


    @Override
    public String toString()
    {
        if (mToString == null)
        {
            mToString = mNamedPlace.name() + ", " + mNamedPlace.extraContext();
        }
        return mToString;
    }
}
