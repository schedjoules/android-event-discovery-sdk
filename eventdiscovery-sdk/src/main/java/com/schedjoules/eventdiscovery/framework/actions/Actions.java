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

package com.schedjoules.eventdiscovery.framework.actions;

import com.schedjoules.client.eventsdiscovery.Event;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.iterables.Repeatable;
import org.dmfs.iterators.AbstractBaseIterator;
import org.dmfs.iterators.AbstractConvertedIterator;
import org.dmfs.iterators.ConvertedIterator;
import org.dmfs.iterators.FilteredIterator;
import org.dmfs.iterators.filters.NonNull;

import java.util.Iterator;


/**
 * An {@link Iterable} of {@link Action}s.
 *
 * @author Marten Gajda
 */
public final class Actions implements Iterable<Action>
{
    private final Iterable<? extends Link> mLinks;
    private final Event mEvent;
    private final ActionFactory mActionFactory;


    public Actions(@android.support.annotation.NonNull Iterator<? extends Link> links, @android.support.annotation.NonNull Event event, @android.support.annotation.NonNull ActionFactory actionFactory)
    {
        this(new Repeatable<>(links), event, actionFactory);
    }


    public Actions(@android.support.annotation.NonNull Iterable<? extends Link> links, @android.support.annotation.NonNull Event event, @android.support.annotation.NonNull ActionFactory actionFactory)
    {
        mLinks = links;
        mEvent = event;
        mActionFactory = actionFactory;
    }


    @android.support.annotation.NonNull
    @Override
    public Iterator<Action> iterator()
    {
        final ActionFactory actionFactory = mActionFactory;
        final Event event = mEvent;
        final Iterator<? extends Link> links = mLinks.iterator();

        return new FilteredIterator<>(new ConvertedIterator<>(new AbstractBaseIterator<Link>()
        {
            @Override
            public boolean hasNext()
            {
                return links.hasNext();
            }


            @Override
            public Link next()
            {
                return links.next();
            }
        }, new AbstractConvertedIterator.Converter<Action, Link>()
        {
            @Override
            public Action convert(Link link)
            {
                return actionFactory.action(link, event);
            }
        }), NonNull.<Action>instance());
    }
}
