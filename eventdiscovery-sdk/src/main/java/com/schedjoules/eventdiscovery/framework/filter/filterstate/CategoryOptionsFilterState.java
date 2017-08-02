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

package com.schedjoules.eventdiscovery.framework.filter.filterstate;

import com.schedjoules.eventdiscovery.framework.filter.categoryoption.CategoryOption;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.SelectedCategories;


/**
 * {@link FilterState} that takes a {@link Iterable<CategoryOption>} to assess whether there is any item selected or not.
 *
 * @author Gabor Keszthelyi
 */
public final class CategoryOptionsFilterState implements FilterState
{
    private final Iterable<CategoryOption> mCategoryOptions;
    private final boolean mIsExpanded;

    private Boolean mCachedHasSelection;


    public CategoryOptionsFilterState(Iterable<CategoryOption> categoryOptions, boolean isExpanded)
    {
        mIsExpanded = isExpanded;
        mCategoryOptions = categoryOptions;
    }


    public CategoryOptionsFilterState(Iterable<CategoryOption> categoryOptions, FilterState original)
    {
        this(categoryOptions, original.isExpanded());
    }


    @Override
    public boolean hasSelection()
    {
        if (mCachedHasSelection == null)
        {
            mCachedHasSelection = new SelectedCategories(mCategoryOptions).iterator().hasNext();
        }
        return mCachedHasSelection;
    }


    @Override
    public boolean isExpanded()
    {
        return mIsExpanded;
    }
}
