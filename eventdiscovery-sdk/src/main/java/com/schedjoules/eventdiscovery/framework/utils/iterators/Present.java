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

package com.schedjoules.eventdiscovery.framework.utils.iterators;

import org.dmfs.iterators.AbstractBaseIterator;
import org.dmfs.iterators.Filter;
import org.dmfs.iterators.Function;
import org.dmfs.iterators.decorators.Fluent;
import org.dmfs.optional.Optional;

import java.util.Iterator;


/**
 * Decorator for {@link Iterator} with {@link Optional} elements to get only the present values.
 *
 * @author Gabor Keszthelyi
 */
public final class Present<E> extends AbstractBaseIterator<E>
{
    private final Iterator<E> mDelegate;


    public Present(Iterator<Optional<E>> iterator)
    {
        mDelegate = new Fluent<>(iterator)
                .filtered(new Filter<Optional<E>>()
                {
                    @Override
                    public boolean iterate(Optional<E> optional)
                    {
                        return optional.isPresent();
                    }
                })
                .mapped(new Function<Optional<E>, E>()
                {
                    @Override
                    public E apply(Optional<E> presentOptional)
                    {
                        return presentOptional.value();
                    }
                });
    }


    @Override
    public boolean hasNext()
    {
        return mDelegate.hasNext();
    }


    @Override
    public E next()
    {
        return mDelegate.next();
    }
}
