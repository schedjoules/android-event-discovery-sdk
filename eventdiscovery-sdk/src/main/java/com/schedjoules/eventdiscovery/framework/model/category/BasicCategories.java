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
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.SimpleLazy;

import org.dmfs.optional.NullSafe;
import org.dmfs.optional.Optional;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.uris.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Basic implementation of {@link Categories}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicCategories implements Categories
{
    private final Iterable<Category> mCategories;
    private final Lazy<Map<String, Category>> mCategoryLabelsMap;


    public BasicCategories(Iterable<Category> categories)
    {
        mCategories = categories;
        mCategoryLabelsMap = new SimpleLazy<>(new Factory<Map<String, Category>>()
        {
            @Override
            public Map<String, Category> create()
            {
                Map<String, Category> result = new HashMap<>();
                for (Category category : mCategories)
                {
                    result.put(new Text(category.name()).toString(), category);
                }
                return result;
            }
        });
    }


    @Override
    public Optional<Category> category(Uri categoryName)
    {
        return new NullSafe<>(mCategoryLabelsMap.get().get(new Text(categoryName).toString()));
    }


    @Override
    public Iterator<Category> iterator()
    {
        return mCategories.iterator();
    }
}