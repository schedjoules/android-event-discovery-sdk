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

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.model.ApiLink;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.iterators.AbstractBaseIterator;
import org.dmfs.iterators.Filter;
import org.dmfs.iterators.decorators.Filtered;

import java.util.Iterator;


/**
 * {@link Iterator} for the {@link Link}s of an {@link Event} that are categories, i.e. have {@link ApiLink.Rel#CATEGORY} reltype.
 *
 * @author Gabor Keszthelyi
 */
public final class EventCategoryLinks extends AbstractBaseIterator<Link>
{
    private final Iterator<Link> mDelegate;


    public EventCategoryLinks(Event event)
    {
        mDelegate = new Filtered<>(event.links().iterator(),
                new Filter<Link>()
                {
                    @Override
                    public boolean iterate(Link element)
                    {
                        return element.relationTypes().contains(ApiLink.Rel.CATEGORY);
                    }
                });
    }


    @Override
    public boolean hasNext()
    {
        return mDelegate.hasNext();
    }


    @Override
    public Link next()
    {
        return mDelegate.next();
    }
}