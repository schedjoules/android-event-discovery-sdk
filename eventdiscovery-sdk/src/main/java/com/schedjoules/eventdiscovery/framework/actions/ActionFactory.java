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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.schedjoules.client.eventsdiscovery.Event;

import org.dmfs.httpessentials.types.Link;


/**
 * A factory to create {@link Action}s for given {@link Event} and action {@link Link}.
 *
 * @author Marten Gajda
 */
public interface ActionFactory
{
    /**
     * Returns an action for the given {@link Link} or {@code null} if the link rel type is not a supported action.
     * <p>
     * TODO: reconsider the null result.
     *
     * @param actionLink
     * @param event
     *
     * @return
     */
    @Nullable
    Action action(@NonNull Link actionLink, @NonNull Event event);
}
