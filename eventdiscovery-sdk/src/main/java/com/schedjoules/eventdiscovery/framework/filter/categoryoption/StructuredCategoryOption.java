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

package com.schedjoules.eventdiscovery.framework.filter.categoryoption;

import com.schedjoules.client.eventsdiscovery.Category;


/**
 * {@link CategoryOption} that simply takes the prepared properties as constructor parameters.
 *
 * @author Gabor Keszthelyi
 */
public final class StructuredCategoryOption implements CategoryOption
{
    private final Category mCategory;
    private final boolean mIsSelected;


    public StructuredCategoryOption(Category category, boolean isSelected)
    {
        mCategory = category;
        mIsSelected = isSelected;
    }


    @Override
    public Category category()
    {
        return mCategory;
    }


    @Override
    public boolean isSelected()
    {
        return mIsSelected;
    }
}
