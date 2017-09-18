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
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.utils.JavaUri;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.iterators.Function;
import org.dmfs.iterators.decorators.DelegatingIterator;
import org.dmfs.iterators.decorators.Mapped;
import org.dmfs.optional.Optional;
import org.dmfs.optional.iterator.PresentValues;

import java.util.Iterator;


/**
 * {@link Iterator} for {@link Category}s of an {@link Event}.
 *
 * @author Gabor Keszthelyi
 */
public final class EventCategories extends DelegatingIterator<Category>
{
    public EventCategories(Iterator<Link> categoryLinks, final Categories categories)
    {
        super(new PresentValues<>(
                new Mapped<>(categoryLinks, new Function<Link, Optional<Category>>()
                {
                    @Override
                    public Optional<Category> apply(Link categoryLink)
                    {
                        return categories.category(new JavaUri(categoryLink.target()));
                    }
                })));
    }


    public EventCategories(Event event, Categories categories)
    {
        this(new EventCategoryLinks(event), categories);
    }
}