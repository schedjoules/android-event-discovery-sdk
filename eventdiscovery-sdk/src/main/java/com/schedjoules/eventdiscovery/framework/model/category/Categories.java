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

import com.schedjoules.client.eventsdiscovery.Category;

import org.dmfs.optional.Optional;
import org.dmfs.rfc3986.Uri;


/**
 * {@link Category}s with support for quick lookup by category name.
 *
 * @author Gabor Keszthelyi
 */
public interface Categories extends Iterable<Category>
{
    /**
     * Looks up the {@link Category} for the given name.
     */
    Optional<Category> category(Uri categoryName);

    /**
     * Returns the categories that can be used for filtering on the UI.
     */
    Iterable<Category> filterCategories();
}