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

import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewFilterItemBinding;
import com.schedjoules.eventdiscovery.framework.filter.filterstate.FilterState;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;
import com.schedjoules.eventdiscovery.framework.widgets.Highlightable;


/**
 * Represents the View for a category filter state item which is selected if no other element is selected.
 *
 * @author Marten Gajda
 */
public final class AllSelectionItemView implements SmartView<FilterState>
{
    private final SmartView<Boolean> mHighlightableLabel;


    public AllSelectionItemView(SchedjoulesViewFilterItemBinding binding)
    {
        mHighlightableLabel = new Highlightable(binding.schedjoulesFilterItemLabel);
    }


    @Override
    public void update(FilterState filterState)
    {
        mHighlightableLabel.update(!filterState.hasSelection());
    }

}
