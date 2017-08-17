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

package com.schedjoules.eventdiscovery.framework.filter.insights;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.insights.steps.AbstractStep;
import com.schedjoules.eventdiscovery.framework.filter.categoryoption.CategoryOption;

import org.dmfs.rfc3986.encoding.Encoded;
import org.dmfs.rfc3986.uris.Text;

import java.net.URI;


/**
 * Category toggled step.
 *
 * @author Marten Gajda
 */
public final class Toggled extends AbstractStep
{
    private final FilterStep mDelegate;
    private final CategoryOption mCategoryOption;


    public Toggled(CategoryOption categoryOption)
    {
        this(new FilterStep(), categoryOption);
    }


    public Toggled(FilterStep delegate, CategoryOption categoryOption)
    {
        mDelegate = delegate;
        mCategoryOption = categoryOption;
    }


    @Override
    public CharSequence category()
    {
        return mDelegate.category();
    }


    @Override
    public URI data()
    {
        URI dataUri = mDelegate.data().resolve(mCategoryOption.isSelected() ? "selected/" : "unselected/");
        return dataUri.resolve(new Encoded(new Text(mCategoryOption.category().name())).toString());
    }


    @Override
    public Event event()
    {
        return mDelegate.event();
    }
}
