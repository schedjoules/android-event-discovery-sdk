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

import com.schedjoules.client.eventsdiscovery.Category;

import org.dmfs.iterables.decorators.Filtered;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Filter;
import org.dmfs.iterators.Function;

import java.util.Iterator;


/**
 * @author Gabor Keszthelyi
 */
public final class SelectedCategories implements Iterable<Category>
{
    private final Iterable<Category> mDelegate;


    public SelectedCategories(final Iterable<CategoryOption> categoryOptions)
    {
        mDelegate = new Mapped<>(
                new Filtered<>(categoryOptions, new Filter<CategoryOption>()
                {
                    @Override
                    public boolean iterate(CategoryOption argument)
                    {
                        return argument.isSelected();
                    }
                }),
                new Function<CategoryOption, Category>()
                {
                    @Override
                    public Category apply(CategoryOption argument)
                    {
                        return argument.category();
                    }
                });
    }


    @Override
    public Iterator<Category> iterator()
    {
        return mDelegate.iterator();
    }
}
