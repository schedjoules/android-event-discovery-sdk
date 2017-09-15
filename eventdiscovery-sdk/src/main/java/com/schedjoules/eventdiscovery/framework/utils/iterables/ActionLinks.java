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

package com.schedjoules.eventdiscovery.framework.utils.iterables;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.iterables.decorators.DelegatingIterable;
import org.dmfs.iterables.decorators.Filtered;
import org.dmfs.iterators.Filter;


/**
 * An Iterable which filters the given {@link Link} {@link Iterable} by a specific rel-type.
 *
 * @author Marten Gajda
 */
public final class ActionLinks extends DelegatingIterable<Link>
{
    public ActionLinks(Iterable<Link> links, final String relType)
    {
        super(new Filtered<Link>(links, new Filter<Link>()
        {
            @Override
            public boolean iterate(Link argument)
            {
                return argument.relationTypes().contains(relType);
            }
        }));
    }
}
