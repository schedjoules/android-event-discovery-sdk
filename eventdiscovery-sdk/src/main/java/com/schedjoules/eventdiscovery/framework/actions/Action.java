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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import org.dmfs.httpessentials.types.Link;


/**
 * Represents an event action that can be executed by the user.
 * <p>
 * Its rel-type is used to match up with {@link Link}s coming from the Actions API.
 *
 * @author Gabor Keszthelyi
 * @author Marten Gajda
 */
public interface Action
{

    /**
     * Returns the short label to display for this {@link Action}. This should return a single word that describes the action.
     *
     * @param context
     *         A {@link Context}
     *
     * @return
     */
    @NonNull
    String shortLabel(@NonNull Context context);

    /**
     * Returns the long label to display for this {@link Action}.
     *
     * @param context
     *         A {@link Context}
     *
     * @return
     */
    @NonNull
    String longLabel(@NonNull Context context);

    /**
     * The icon to display for this Action.
     *
     * @return A {@link Drawable} of the icon.
     */
    @NonNull
    Drawable icon(@NonNull Context context);

    /**
     * The executable command to carry out when the user activates (taps on) this Action.
     *
     * @return an executable command that the will start the action when executed, usually when it is clicked
     */
    @NonNull
    ActionExecutable actionExecutable();
}
