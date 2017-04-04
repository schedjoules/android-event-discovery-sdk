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
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.optionals.AbstractCachingOptional;
import com.schedjoules.eventdiscovery.framework.utils.optionals.First;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.iterators.AbstractConvertedIterator;
import org.dmfs.iterators.AbstractFilteredIterator;
import org.dmfs.iterators.ConvertedIterator;
import org.dmfs.iterators.FilteredIterator;
import org.dmfs.optional.Optional;


/**
 * {@link Optional<Action>} that looks for the {@link Action} in a provided {@link Iterable<Action>} by link rel type.
 *
 * @author Gabor Keszthelyi
 */
public final class OptionalAction extends AbstractCachingOptional<Action>
{

    public OptionalAction(final String actionLinkRelType, final Iterable<Link> actionLinks, final ActionFactory actionFactory, final Event event)
    {
        super(new Factory<Optional<Action>>()
        {
            @Override
            public Optional<Action> create()
            {
                return new First<>(
                        new ConvertedIterator<Action, Link>(
                                new FilteredIterator<>(actionLinks.iterator(),
                                        new AbstractFilteredIterator.IteratorFilter<Link>()
                                        {
                                            @Override
                                            public boolean iterate(Link link)
                                            {
                                                return link.relationTypes().contains(actionLinkRelType);
                                            }
                                        }), new AbstractConvertedIterator.Converter<Action, Link>()
                        {
                            @Override
                            public Action convert(Link element)
                            {
                                return actionFactory.action(element, event);
                            }
                        }));

            }
        });
    }

}
