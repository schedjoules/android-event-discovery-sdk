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

package com.schedjoules.eventdiscovery.framework.filter.categoryoption;

import com.schedjoules.eventdiscovery.framework.utils.equalator.UriEqualator;

import org.dmfs.iterables.Repeatable;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Function;

import java.util.Iterator;


/**
 * {@link Iterable<CategoryOption>} which updates the input {@link Iterable<CategoryOption>} with the given
 * {@link CategoryOption}, i.e. replaces the corresponding one in the {@link Iterable}.
 *
 * @author Gabor Keszthelyi
 */
public final class Updated implements Iterable<CategoryOption>
{
    private final Iterable<CategoryOption> mDelegate;


    public Updated(Iterable<CategoryOption> original, final CategoryOption update)
    {
        mDelegate = new Repeatable<>(new Mapped<>(original, new Function<CategoryOption, CategoryOption>()
        {
            @Override
            public CategoryOption apply(CategoryOption catOption)
            {
                if (UriEqualator.INSTANCE.areEqual(catOption.category().name(), update.category().name()))
                {
                    return update;
                }
                return catOption;
            }
        }).iterator());
    }


    @Override
    public Iterator<CategoryOption> iterator()
    {
        return mDelegate.iterator();
    }
}
