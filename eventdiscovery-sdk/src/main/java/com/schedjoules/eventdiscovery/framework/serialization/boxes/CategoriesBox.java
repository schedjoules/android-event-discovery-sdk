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
import com.schedjoules.eventdiscovery.framework.model.category.BasicCategories;
import com.schedjoules.eventdiscovery.framework.model.category.Categories;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;


/**
 * {@link Box} for {@link Categories}.
 *
 * @author Gabor Keszthelyi
 */
public final class CategoriesBox implements Box<Categories>
{
    private final Categories mCategories;


    public CategoriesBox(Categories categories)
    {
        mCategories = categories;
    }


    @Override
    public Categories content()
    {
        return mCategories;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(new IterableBox<>(mCategories, CategoryBox.FACTORY), flags);
    }


    public static final Creator<CategoriesBox> CREATOR = new Creator<CategoriesBox>()
    {
        @Override
        public CategoriesBox createFromParcel(Parcel in)
        {
            IterableBox<Category> categoryIterableBox = in.readParcelable(getClass().getClassLoader());
            return new CategoriesBox(new BasicCategories(categoryIterableBox.content()));
        }


        @Override
        public CategoriesBox[] newArray(int size)
        {
            return new CategoriesBox[size];
        }
    };
}
