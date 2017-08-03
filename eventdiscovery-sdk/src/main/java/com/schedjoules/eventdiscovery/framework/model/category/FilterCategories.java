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

package com.schedjoules.eventdiscovery.framework.model.category;

import com.schedjoules.client.eventsdiscovery.Category;

import org.dmfs.iterables.decorators.Filtered;
import org.dmfs.iterators.Filter;
import org.dmfs.rfc3986.Path;
import org.dmfs.rfc3986.UriEncoded;

import java.util.Iterator;


/**
 * The {@link Category}s that can be used for filtering on the UI.
 *
 * @author Gabor Keszthelyi
 */
public final class FilterCategories implements Iterable<Category>
{
    private final Iterable<Category> mDelegate;


    public FilterCategories(Iterable<Category> allCategories)
    {
        mDelegate = new Filtered<>(allCategories, new Filter<Category>()
        {
            @Override
            public boolean iterate(Category category)
            {
                Path path = category.name().path();
                int numberOfPathElements = 0;
                for (UriEncoded element : path)
                {
                    numberOfPathElements++;
                }
                return numberOfPathElements < 4;
            }
        });
    }


    @Override
    public Iterator<Category> iterator()
    {
        return mDelegate.iterator();
    }
}
