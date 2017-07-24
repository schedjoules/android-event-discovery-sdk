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

import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Function;
import org.dmfs.rfc3986.Uri;

import java.util.Iterator;


/**
 * {@link Iterable} of {@link Uri}s of {@link Category#name()} from an input {@link Iterable} of {@link Category}s.
 *
 * @author Gabor Keszthelyi
 */
public final class CategoryUris implements Iterable<Uri>
{
    private final Iterable<Uri> mDelegate;


    public CategoryUris(Iterable<Category> categories)
    {
        mDelegate = new Mapped<>(categories, new Function<Category, Uri>()
        {
            @Override
            public Uri apply(Category category)
            {
                return category.name();
            }
        });
    }


    @Override
    public Iterator<Uri> iterator()
    {
        return mDelegate.iterator();
    }
}
