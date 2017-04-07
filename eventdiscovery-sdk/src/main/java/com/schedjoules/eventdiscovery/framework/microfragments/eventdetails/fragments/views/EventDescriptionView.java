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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.views;

import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewEventDetailsDescriptionBinding;
import com.schedjoules.eventdiscovery.framework.utils.OptionalView;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;
import com.schedjoules.eventdiscovery.framework.widgets.LinkifyingExpandableTextView;

import org.dmfs.optional.Optional;


/**
 * Represents the View for the event description.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDescriptionView implements SmartView<Optional<String>>
{
    private final OptionalView mRoot;
    private final LinkifyingExpandableTextView mTitleView;


    public EventDescriptionView(SchedjoulesViewEventDetailsDescriptionBinding binding)
    {
        mRoot = new OptionalView(binding.getRoot());
        mTitleView = new LinkifyingExpandableTextView(binding.schedjoulesEventDetailsItemTitle);
    }


    @Override
    public void update(Optional<String> description)
    {
        mRoot.update(description);
        mTitleView.update(description.value(null));
    }
}
