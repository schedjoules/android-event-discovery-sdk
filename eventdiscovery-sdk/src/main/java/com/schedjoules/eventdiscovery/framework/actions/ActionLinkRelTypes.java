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

/**
 * The supported action link rel types sent by the Actions API.
 *
 * @author Gabor Keszthelyi
 */
public final class ActionLinkRelTypes
{
    public static final String BOOK = "http://schedjoules.com/rel/action/book";
    public static final String ADD_TO_CALENDAR = "http://schedjoules.com/rel/action/add-to-calendar";
    public static final String SHARE = "http://schedjoules.com/rel/action/share";
    public static final String DIRECTIONS = "http://schedjoules.com/rel/action/directions";
    public static final String INFO = "http://schedjoules.com/rel/action/info";
    public static final String CALL = "http://schedjoules.com/rel/action/call";
    public static final String BUY = "http://schedjoules.com/rel/action/buy";
    public static final String REMIND = "http://schedjoules.com/rel/action/remind";
    public static final String WATCH = "http://schedjoules.com/rel/action/watch";
    public static final String INVITE = "http://schedjoules.com/rel/action/invite";


    private ActionLinkRelTypes()
    {
    }
}
