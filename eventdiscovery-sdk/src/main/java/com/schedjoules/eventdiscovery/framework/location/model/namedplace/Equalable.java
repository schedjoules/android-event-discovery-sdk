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
 * {@link com.schedjoules.eventdiscovery.framework.model.Equalable} decorator for {@link NamedPlace}, value is based on id().
 *
 * @author Gabor Keszthelyi
 */
public final class Equalable implements NamedPlace, com.schedjoules.eventdiscovery.framework.model.Equalable
{
    private final NamedPlace mNamedPlace;


    public Equalable(NamedPlace namedPlace)
    {
        mNamedPlace = namedPlace;
    }


    @Override
    public String id()
    {
        return mNamedPlace.id();
    }


    @Override
    public CharSequence name()
    {
        return mNamedPlace.name();
    }


    @Override
    public CharSequence extraContext()
    {
        return mNamedPlace.extraContext();
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Equalable equalable = (Equalable) o;

        return id().equals(equalable.id());

    }


    @Override
    public int hashCode()
    {
        return id().hashCode();
    }
}
