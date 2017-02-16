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

package com.schedjoules.eventdiscovery.framework.list;

/**
 * Action that can be executed when a list item was chosen by the user.
 *
 * @author Gabor Keszthelyi
 */
public interface ItemChosenAction<T>
{

    /**
     * Called when the item was chosen by the user.
     *
     * @param itemData
     *         the data corresponding to the list item
     */
    void onItemChosen(T itemData);
}
