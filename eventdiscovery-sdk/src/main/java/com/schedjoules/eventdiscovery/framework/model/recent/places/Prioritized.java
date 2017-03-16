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

package com.schedjoules.eventdiscovery.framework.model.recent.places;

import com.schedjoules.eventdiscovery.framework.model.recent.Recent;
import com.schedjoules.eventdiscovery.framework.model.recent.Recents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * A {@link Recents} decorator that returns the recent values sorted by their timestamp (highest timestamp first).
 *
 * @author Marten Gajda
 */
public final class Prioritized<T> implements Recents<T>
{
    private final Recents<T> mDelegate;


    public Prioritized(Recents<T> delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public Recent<T> recent(T object)
    {
        return mDelegate.recent(object);
    }


    @Override
    public Iterator<Recent<T>> iterator()
    {
        List<Recent<T>> recents = new ArrayList<>(10);
        for (Recent<T> r : mDelegate)
        {
            recents.add(r);
        }
        Collections.sort(recents, new Comparator<Recent<T>>()
        {
            @Override
            public int compare(Recent<T> lhs, Recent<T> rhs)
            {
                // The one with the higher timestamp comes first!
                return lhs.timestamp() < rhs.timestamp() ? 1 : lhs.timestamp() > rhs.timestamp() ? -1 : 0;
            }
        });
        return recents.iterator();
    }
}
