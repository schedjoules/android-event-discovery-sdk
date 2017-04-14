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

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewEventDetailsVenueBinding;
import com.schedjoules.eventdiscovery.framework.utils.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.OptionalView;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;

import org.dmfs.optional.Optional;


/**
 * Represents the venue name view on the event details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class VenueNameView implements SmartView<Optional<CharSequence>>
{
    private final OptionalView mRoot;
    private final TextView mTextView;
    private final TextView mIconAnchorTextView;


    public VenueNameView(SchedjoulesViewEventDetailsVenueBinding binding)
    {
        mRoot = new OptionalView(binding.getRoot());
        mTextView = binding.schedjoulesVenueName;
        mIconAnchorTextView = binding.schedjoulesVenueNameIconAnchor;
    }


    @Override
    public void update(Optional<CharSequence> venueName)
    {
        mRoot.update(venueName);
        mTextView.setText(venueName.value(null));

        if (venueName.isPresent())
        {
            applyCompoundDrawableTint(mIconAnchorTextView,
                    new AttributeColor(mIconAnchorTextView.getContext(), android.support.v7.appcompat.R.attr.colorAccent).argb());
            applyCompoundDrawableTint(mTextView, 0 /* transparent */);
        }
    }


    // TODO Something like this could be created: DrawableTintCompatTextView extends AppCompatTextView with attr compat:drawableTint
    private void applyCompoundDrawableTint(TextView textView, int color)
    {
        Drawable[] oldDrawables = textView.getCompoundDrawables();
        Drawable[] newDrawables = new Drawable[4];
        for (int i = 0; i < 4; i++)
        {
            if (oldDrawables[i] != null)
            {
                Drawable mutated = DrawableCompat.wrap(oldDrawables[i]);
                mutated.mutate();
                DrawableCompat.setTint(mutated, color);
                newDrawables[i] = mutated;
            }
        }
        textView.setCompoundDrawables(newDrawables[0], newDrawables[1], newDrawables[2], newDrawables[3]);
    }
}
