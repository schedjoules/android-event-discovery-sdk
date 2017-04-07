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
import org.dmfs.optional.Present;


/**
 * {@link Optional} that maps an input {@link Optional} using a converter.
 *
 * @author Gabor Keszthelyi
 */
public final class Mapped<FROM, TO> extends AbstractCachingOptional<TO>
{
    public Mapped(final Optional<FROM> optionalFrom, final Converter<FROM, TO> converter)
    {
        super(new Factory<Optional<TO>>()
        {
            @Override
            public Optional<TO> create()
            {
                return optionalFrom.isPresent() ? new Present<TO>(converter.convert(optionalFrom.value())) : Absent.<TO>absent();
            }
        });
    }

}
