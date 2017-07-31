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

package com.schedjoules.eventdiscovery.framework.common;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.schedjoules.client.eventsdiscovery.Category;
import com.schedjoules.eventdiscovery.framework.model.category.Categories;
import com.schedjoules.eventdiscovery.framework.utils.ScopedCacheable;

import org.dmfs.optional.Optional;
import org.dmfs.rfc3986.Uri;

import java.util.Iterator;


/**
 * {@link ViewModel} holding the cached {@link Categories}.
 *
 * @author Gabor Keszthelyi
 */
public final class CategoriesCache extends ViewModel implements Categories, ScopedCacheable<FragmentActivity>
{
    private final Categories mCache;


    public CategoriesCache(Categories categories)
    {
        mCache = categories;
    }


    public CategoriesCache(FragmentActivity activity)
    {
        this(ViewModelProviders.of(activity).get(CategoriesCache.class));
    }


    @Override
    public Optional<Category> category(Uri categoryName)
    {
        return mCache.category(categoryName);
    }


    @Override
    public Iterable<Category> filterCategories()
    {
        return mCache.filterCategories();
    }


    @Override
    public Iterator<Category> iterator()
    {
        return mCache.iterator();
    }


    @Override
    public void cache(FragmentActivity activity)
    {
        ViewModelProviders.of(activity,
                new ViewModelProvider.Factory()
                {
                    @Override
                    public <T extends ViewModel> T create(Class<T> modelClass)
                    {
                        //noinspection unchecked
                        return (T) new CategoriesCache(mCache);
                    }
                })
                // This get() call creates the ViewHolder instance that will be accessed elsewhere:
                .get(CategoriesCache.class);
    }
}
