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

package com.schedjoules.eventdiscovery.framework.serialization.core;

import org.dmfs.optional.Optional;


/**
 * Interface for builders that use {@link Key} and {@link Box} to add parameters to their built result object.
 * <p>
 * Implementations are expected to be immutable.
 *
 * @author Gabor Keszthelyi
 */
public interface FluentBuilder<R>
{
    /**
     * Creates the new parametrized object.
     */
    R build();

    /**
     * Adds the given parameter to the underlying parametrized object.
     *
     * @param key
     *         the key for this parameter
     * @param box
     *         the box 'containing' the value
     * @param <T>
     *         type of the value
     *
     * @return new instance of this builder with new instance of the underlying parametrized object
     */
    <T> FluentBuilder<R> with(Key<T> key, Box<T> box);

    /**
     * Same as {@link #with(Key, Box)} but with taking an {@link Optional<Box>}.
     * If that is absent, no change is applied.
     */
    <T> FluentBuilder<R> with(Key<T> key, Optional<Box<T>> optBox);

    /**
     * Same as {@link #with(Key, Box)} but with taking an {@link Optional<Boxable>} to take the {@link Box} from.
     * If that is absent, no change is applied.
     */
    <T extends Boxable> FluentBuilder<R> withBoxable(Key<T> key, Optional<T> optBoxable);
}
