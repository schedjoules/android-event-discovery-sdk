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

import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Function;

import java.util.Iterator;


/**
 * {@link Iterable<CategoryOption>} that un-selects (clears) all for the given input {@link Iterable<CategoryOption>}.
 *
 * @author Gabor Keszthelyi
 */
public final class ClearedSelection implements Iterable<CategoryOption>
{
    private final Iterable<CategoryOption> mDelegate;


    public ClearedSelection(Iterable<CategoryOption> categoryOptions)
    {
        mDelegate = new Mapped<>(categoryOptions, new Function<CategoryOption, CategoryOption>()
        {
            @Override
            public CategoryOption apply(CategoryOption categoryOption)
            {
                return new StructuredCategoryOption(categoryOption.category(), false);
            }
        });
    }


    @Override
    public Iterator<CategoryOption> iterator()
    {
        return mDelegate.iterator();
    }
}
