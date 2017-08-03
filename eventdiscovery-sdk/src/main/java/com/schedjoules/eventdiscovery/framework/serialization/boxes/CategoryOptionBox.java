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

package com.schedjoules.eventdiscovery.framework.serialization.boxes;

import android.os.Parcel;

import com.schedjoules.client.eventsdiscovery.Category;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.CategoryOption;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.StructuredCategoryOption;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BoxFactory;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;


/**
 * {@link Box} for {@link CategoryOption}.
 *
 * @author Gabor Keszthelyi
 */
public final class CategoryOptionBox implements Box<CategoryOption>
{
    private final CategoryOption mCategoryOption;


    public CategoryOptionBox(CategoryOption categoryOption)
    {
        mCategoryOption = categoryOption;
    }


    @Override
    public CategoryOption content()
    {
        return mCategoryOption;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(new CategoryBox(mCategoryOption.category()), flags);
        dest.writeParcelable(new BooleanBox(mCategoryOption.isSelected()), flags);
    }


    public static final Creator<CategoryOptionBox> CREATOR = new Creator<CategoryOptionBox>()
    {
        @Override
        public CategoryOptionBox createFromParcel(Parcel in)
        {
            ClassLoader classLoader = getClass().getClassLoader();
            Box<Category> categoryBox = in.readParcelable(classLoader);
            Box<Boolean> isSelectedBox = in.readParcelable(classLoader);
            return new CategoryOptionBox(new StructuredCategoryOption(categoryBox.content(), isSelectedBox.content()));
        }


        @Override
        public CategoryOptionBox[] newArray(int size)
        {
            return new CategoryOptionBox[size];
        }
    };

    public static final BoxFactory<CategoryOption> FACTORY = new BoxFactory<CategoryOption>()
    {
        @Override
        public Box<CategoryOption> create(CategoryOption value)
        {
            return new CategoryOptionBox(value);
        }
    };
}
