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

package com.schedjoules.eventdiscovery.framework.utils.optionals;

import com.schedjoules.eventdiscovery.framework.utils.Converter;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;

import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;


/**
 * An {@link Optional} that maps in input {@link Optional} to another {@link Optional} using the provided converter.
 *
 * @author Gabor Keszthelyi
 */
public final class OptionalMapped<FROM, TO> extends AbstractCachingOptional<TO>
{
    public OptionalMapped(final Optional<FROM> optionalFrom, final Converter<FROM, Optional<TO>> converter)
    {
        super(new Factory<Optional<TO>>()
        {
            @Override
            public Optional<TO> create()
            {
                return optionalFrom.isPresent() ? converter.convert(optionalFrom.value()) : Absent.<TO>absent();
            }
        });
    }
}
