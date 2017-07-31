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

/**
 * {@link FilterState} that takes the ready properties as constructor parameters.
 *
 * @author Gabor Keszthelyi
 */
public final class StructuredFilterState implements FilterState
{
    private final boolean mHasSelection;
    private final boolean mIsExpanded;


    public StructuredFilterState(boolean hasSelection, boolean isExpanded)
    {
        mHasSelection = hasSelection;
        mIsExpanded = isExpanded;
    }


    @Override
    public boolean hasSelection()
    {
        return mHasSelection;
    }


    @Override
    public boolean isExpanded()
    {
        return mIsExpanded;
    }
}
