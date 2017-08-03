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
 * {@link CategoryOption} decorator negating {@link CategoryOption#isSelected()}.
 *
 * @author Gabor Keszthelyi
 */
public final class SelectionToggled implements CategoryOption
{
    private final CategoryOption mDelegate;


    public SelectionToggled(CategoryOption original)
    {
        mDelegate = new StructuredCategoryOption(original.category(), !original.isSelected());
    }


    @Override
    public Category category()
    {
        return mDelegate.category();
    }


    @Override
    public boolean isSelected()
    {
        return mDelegate.isSelected();
    }
}
