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
import org.dmfs.iterators.Function;


/**
 * A Function which converts {@link Link}s of a specific {@link Event} into {@link Action}s using the given {@link ActionFactory}
 *
 * @author Marten Gajda
 */
public final class ActionFunction implements Function<Link, Action>
{
    private final ActionFactory mActionFactory;
    private final Event mEvent;


    public ActionFunction(ActionFactory actionFactory, Event event)
    {
        mActionFactory = actionFactory;
        mEvent = event;
    }


    @Override
    public Action apply(Link argument)
    {
        return mActionFactory.action(argument, mEvent);
    }
}
