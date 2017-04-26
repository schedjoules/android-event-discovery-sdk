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

package com.schedjoules.eventdiscovery.framework.widgets;

import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.utils.charsequence.Trimmed;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;
import com.schedjoules.eventdiscovery.framework.utils.spanned.Html;
import com.schedjoules.eventdiscovery.framework.utils.spanned.Linkified;

import org.dmfs.optional.Optional;


/**
 * Decorator-like class for {@link ExpandableTextView} to add html rendering and linkification.
 *
 * @author Gabor Keszthelyi
 */
public class LinkifyingExpandableTextView implements SmartView<Optional<String>>
{
    private final ExpandableTextView mExpandableTextView;


    public LinkifyingExpandableTextView(ExpandableTextView expandableTextView)
    {
        mExpandableTextView = expandableTextView;

        /*
         * Note: {@link LinkMovementMethod} causes a glitch, the content to scroll down a bit when a link that overflows
         * the last line of collapsed state is clicked, that's why {@link LinkOpeningTouchListener} is used instead.
         * See the javadoc there as well.
         */
        TextView textView = (TextView) mExpandableTextView.findViewById(R.id.expandable_text);
        textView.setOnTouchListener(new LinkOpeningTouchListener());
    }


    @Override
    public void update(Optional<String> optInput)
    {
        if (optInput.isPresent())
        {
            String input = optInput.value();
            // only parse as HTML if the string contains HTML
            mExpandableTextView.setText(isHtml(input) ?
                    new Html(input) : new Linkified(new Trimmed(input)));
        }
    }


    private boolean isHtml(String input)
    {
        return input.contains("</") || input.contains("/>") || input.contains("<br>");
    }
}
