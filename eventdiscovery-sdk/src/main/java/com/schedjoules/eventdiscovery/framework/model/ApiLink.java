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

package com.schedjoules.eventdiscovery.framework.model;

import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.parameters.BasicParameterType;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.typedentity.EntityConverter;


/**
 * The link keys and values supported by the API and used in the SDK.
 * <p>
 * Can be double-checked against <a href="https://schedjoules.github.io/event-discovery-api/">API documentation</a>
 *
 * @author Gabor Keszthelyi
 */
public final class ApiLink
{

    public static final class Rel
    {
        public static final String THUMBNAIL = "http://schedjoules.com/rel/thumbnail";
        public static final String BANNER = "http://schedjoules.com/rel/banner";
        public static final String CATEGORY = "http://schedjoules.com/rel/category";


        public static final class Action
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
        }
    }


    public static final class Prop
    {
        private static final EntityConverter<String> STRING_CONVERTER = new PlainStringHeaderConverter();

        public static final ParameterType<String> WIDTH = new BasicParameterType<>("http://schedjoules.com/prop/width", STRING_CONVERTER);
        public static final ParameterType<String> HEIGHT = new BasicParameterType<>("http://schedjoules.com/prop/height", STRING_CONVERTER);


        public static final class Booking
        {
            public static final ParameterType<String> MIN_PRICE = new BasicParameterType<>("http://schedjoules.com/props/booking/min-price", STRING_CONVERTER);
            public static final ParameterType<String> MAX_PRICE = new BasicParameterType<>("http://schedjoules.com/props/booking/max-price", STRING_CONVERTER);
            public static final ParameterType<String> TYPE = new BasicParameterType<>("http://schedjoules.com/props/booking/type", STRING_CONVERTER);
            public static final String TYPE_VALUE_TICKET = "ticket";
        }

    }

}
