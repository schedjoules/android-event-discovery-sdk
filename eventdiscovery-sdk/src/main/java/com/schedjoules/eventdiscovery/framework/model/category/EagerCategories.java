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

package com.schedjoules.eventdiscovery.framework.model.category;

import android.support.annotation.WorkerThread;

import com.schedjoules.client.eventsdiscovery.Category;

import org.dmfs.optional.Optional;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * {@link Categories} that prepares underlying data eagerly so that it can be accessed faster on the UI thread later.
 *
 * @author Gabor Keszthelyi
 */
// TODO Move this to a factory https://github.com/schedjoules/android-event-discovery-sdk/issues/349
public final class EagerCategories implements Categories
{
    private final Categories mCategoriesDelegate;

    private final List<Category> mFilterCategoriesDelegate;


    @WorkerThread
    public EagerCategories(Iterable<Category> categories)
    {
        mCategoriesDelegate = new LazyCategories(categories);

        // Call into {@link BasicCategories#category()} to initialize the Map.
        mCategoriesDelegate.category(new LazyUri(new Precoded("http://notused")));

        // Preload the filter categories into a List:
        Iterable<Category> filterCategories = new FilterCategories(categories);
        mFilterCategoriesDelegate = new LinkedList<>();
        for (Category category : filterCategories)
        {
            mFilterCategoriesDelegate.add(category);
        }
    }


    @Override
    public Optional<Category> category(Uri categoryName)
    {
        return mCategoriesDelegate.category(categoryName);
    }


    @Override
    public Iterator<Category> iterator()
    {
        return mCategoriesDelegate.iterator();
    }


    @Override
    public Iterable<Category> filterCategories()
    {
        return mFilterCategoriesDelegate;
    }
}