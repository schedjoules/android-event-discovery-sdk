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

package com.schedjoules.eventdiscovery.framework.filter.categoryoption;

import com.schedjoules.client.eventsdiscovery.Category;

import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;

import java.util.Arrays;
import java.util.List;


/**
 * Temporary.
 *
 * @author Gabor Keszthelyi
 */
public final class DummyCategories
{

    public static final List<Category> CATS = Arrays.asList(
            c("Music"),
            c("Sports"),
            c("Movies"),
            c("Theatre"),
            c("Business"),
            c("Attractions")
    );


    private static final Category c(final String label)
    {
        return new Category()
        {
            @Override
            public Uri name()
            {
                return new LazyUri(new Precoded("http://" + label));
            }


            @Override
            public CharSequence label()
            {
                return label;
            }
        };
    }

}
