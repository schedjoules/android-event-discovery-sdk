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

import android.view.View;

import com.schedjoules.client.eventsdiscovery.Category;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewFilterItemBinding;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.CategoryOption;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.StructuredCategoryOption;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;
import com.schedjoules.eventdiscovery.framework.widgets.Highlightable;


/**
 * Represents the View for a category filter item.
 *
 * @author Gabor Keszthelyi
 */
public final class FilterItemView implements SmartView<CategoryOption>
{
    private final SchedjoulesViewFilterItemBinding mBinding;
    private final CategoryClickListener mCategoryClickListener;
    private final SmartView<Boolean> mHighightableLabel;


    public FilterItemView(SchedjoulesViewFilterItemBinding binding, CategoryClickListener categoryClickListener)
    {
        mBinding = binding;
        mCategoryClickListener = categoryClickListener;
        mHighightableLabel = new Highlightable(mBinding.schedjoulesFilterItemLabel);
    }


    @Override
    public void update(CategoryOption categoryOption)
    {
        mHighightableLabel.update(categoryOption.isSelected());

        mBinding.schedjoulesFilterItemLabel.setText(categoryOption.category().label());
        mBinding.getRoot().setOnClickListener(new ClickListener(categoryOption.category()));
    }


    private class ClickListener implements View.OnClickListener
    {
        private final Category mCategory;

        private boolean mIsSelected;


        public ClickListener(Category category)
        {
            mCategory = category;
        }


        @Override
        public void onClick(View v)
        {
            mIsSelected = !mIsSelected;
            mHighightableLabel.update(mIsSelected);
            mCategoryClickListener.onClick(new StructuredCategoryOption(mCategory, mIsSelected));
        }
    }
}
