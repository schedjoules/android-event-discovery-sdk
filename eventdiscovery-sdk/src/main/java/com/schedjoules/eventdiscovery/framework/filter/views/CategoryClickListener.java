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

package com.schedjoules.eventdiscovery.framework.filter.views;

import com.schedjoules.client.eventsdiscovery.Category;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.CategoryOption;


/**
 * Listener interface to list to {@link Category} selection clicks.
 *
 * @author Gabor Keszthelyi
 */
public interface CategoryClickListener
{

    /**
     * Called when a Category is clicked.
     *
     * @param categoryOption
     *         the category and whether it is selected or not in its new state
     */
    void onClick(CategoryOption categoryOption);
}
