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
import com.schedjoules.eventdiscovery.framework.model.category.StructuredCategory;
import com.schedjoules.eventdiscovery.framework.serialization.commons.BoxFactory;
import com.schedjoules.eventdiscovery.framework.serialization.core.Box;

import org.dmfs.rfc3986.Uri;


/**
 * {@link Box} for {@link Category}.
 *
 * @author Gabor Keszthelyi
 */
public final class CategoryBox implements Box<Category>
{

    private final Category mCategory;


    public CategoryBox(Category category)
    {
        mCategory = category;
    }


    @Override
    public Category content()
    {
        return mCategory;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(new UriBox(mCategory.name()), flags);
        dest.writeString(mCategory.label().toString());
    }


    public static final Creator<CategoryBox> CREATOR = new Creator<CategoryBox>()
    {
        @Override
        public CategoryBox createFromParcel(Parcel in)
        {
            Box<Uri> uriBox = in.readParcelable(getClass().getClassLoader());
            CharSequence label = in.readString();
            return new CategoryBox(new StructuredCategory(uriBox.content(), label));
        }


        @Override
        public CategoryBox[] newArray(int size)
        {
            return new CategoryBox[size];
        }
    };

    public static final BoxFactory<Category> FACTORY = new BoxFactory<Category>()
    {
        @Override
        public Box<Category> create(Category category)
        {
            return new CategoryBox(category);
        }
    };
}
