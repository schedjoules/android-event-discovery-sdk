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

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewFilterItemBinding;
import com.schedjoules.eventdiscovery.framework.common.CategoriesCache;
import com.schedjoules.eventdiscovery.framework.filter.CategorySelectionChangeListener;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.CategoryOption;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.ClearedSelection;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.SelectedCategories;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.UnselectedCategories;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.Updated;
import com.schedjoules.eventdiscovery.framework.filter.filterstate.CategoryOptionsFilterState;
import com.schedjoules.eventdiscovery.framework.filter.filterstate.ExpandNegated;
import com.schedjoules.eventdiscovery.framework.filter.filterstate.FilterState;
import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.CategoryOptionBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.IterableBox;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.Argument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BundleBuilder;
import com.schedjoules.eventdiscovery.framework.utils.ContextActivity;
import com.schedjoules.eventdiscovery.framework.utils.Listenable;
import com.schedjoules.eventdiscovery.framework.utils.colors.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;
import com.schedjoules.eventdiscovery.framework.widgets.Highlightable;


/**
 * Custom View for the Event filter bar.
 *
 * @author Gabor Keszthelyi
 */
public final class EventFilterView extends LinearLayout implements Listenable<CategorySelectionChangeListener>
{
    private CategoryClickListener mCategorySelectListener;

    private SmartView<FilterState> mTitleView;
    private TextView mCategoryTitle;
    private PopupWindow mPopup;

    private Iterable<CategoryOption> mCategoryOptions;
    private FilterState mFilterState;

    private CategorySelectionChangeListener mCategorySelectionChangeListener;
    private LinearLayout mDropDown;


    public EventFilterView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mCategoryTitle = (TextView) findViewById(R.id.schedjoules_event_list_filter_category_title);
        mCategorySelectListener = new CategorySelectListener();

        mDropDown = new LinearLayout(getContext());
        mDropDown.setOrientation(LinearLayout.VERTICAL);
        mPopup = new PopupWindow(mDropDown, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopup.setBackgroundDrawable(new ColorDrawable(new AttributeColor(getContext(), android.R.attr.windowBackground).argb()));
        mPopup.setOutsideTouchable(true);

        mCategoryTitle.setOnClickListener(new TitleClickListener());

        mTitleView = new FilterTitleView(mCategoryTitle, R.string.schedjoules_category_filter_title);

        update(new UnselectedCategories(
                new CategoriesCache(new ContextActivity(this).get()).filterCategories()));
    }


    private void update(Iterable<CategoryOption> categoryOptions)
    {
        mCategoryOptions = categoryOptions;
        mFilterState = new CategoryOptionsFilterState(mCategoryOptions, false);
        mTitleView.update(mFilterState);

        // Populate the drop-down
        mDropDown.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (CategoryOption categoryOption : categoryOptions)
        {
            inflater.inflate(R.layout.schedjoules_view_divider, mDropDown, true);
            SchedjoulesViewFilterItemBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.schedjoules_view_filter_item, mDropDown, true);
            new FilterItemView(itemBinding, mCategorySelectListener).update(categoryOption);
        }
        inflater.inflate(R.layout.schedjoules_view_divider, mDropDown, true);

        // Add Clear item
        SchedjoulesViewFilterItemBinding clearItem = DataBindingUtil.inflate(inflater, R.layout.schedjoules_view_filter_item, mDropDown, true);
        clearItem.schedjoulesFilterItemLabel.setText(R.string.schedjoules_filter_clear);
        clearItem.getRoot().setOnClickListener(new ClearSelectListener());
        new Highlightable(clearItem.schedjoulesFilterItemLabel).update(false); // To remove background
        inflater.inflate(R.layout.schedjoules_view_divider, mDropDown, true);
    }


    @Override
    protected Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        return new BundleBuilder()
                .with(Keys.SUPER_STATE, new ParcelableBox<>(superState))
                .with(Keys.CATEGORY_OPTIONS, new IterableBox<>(mCategoryOptions, CategoryOptionBox.FACTORY))
                .build();
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        super.onRestoreInstanceState(new Argument<>(Keys.SUPER_STATE, (Bundle) state).get());
        update(new Argument<>(Keys.CATEGORY_OPTIONS, (Bundle) state).get());
    }


    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mPopup.dismiss(); // PopUpWindow may get attached to Activity, so we need to dismiss to prevent leak
    }


    @Override
    public void listen(CategorySelectionChangeListener listener)
    {
        mCategorySelectionChangeListener = listener;
    }


    private class CategorySelectListener implements CategoryClickListener
    {
        @Override
        public void onClick(CategoryOption categoryOption)
        {
            mCategoryOptions = new Updated(mCategoryOptions, categoryOption);
            mFilterState = new CategoryOptionsFilterState(mCategoryOptions, mFilterState);
            mTitleView.update(mFilterState);

            if (mCategorySelectionChangeListener != null)
            {
                mCategorySelectionChangeListener.onCategorySelectionChanged(new SelectedCategories(mCategoryOptions));
            }
        }
    }


    private class ClearSelectListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            mPopup.dismiss();
            update(new ClearedSelection(mCategoryOptions));

            if (mCategorySelectionChangeListener != null)
            {
                mCategorySelectionChangeListener.onCategorySelectionChanged(new SelectedCategories(mCategoryOptions));
            }
        }
    }


    private class TitleClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            mFilterState = new ExpandNegated(mFilterState);
            if (!mFilterState.isExpanded())
            {
                mPopup.dismiss();
            }
            else
            {
                mPopup.showAsDropDown(EventFilterView.this);
            }
        }
    }
}
