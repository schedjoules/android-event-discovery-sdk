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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewEventDetailsVenueBinding;
import com.schedjoules.eventdiscovery.framework.utils.OptionalView;
import com.schedjoules.eventdiscovery.framework.utils.TintedDrawable;
import com.schedjoules.eventdiscovery.framework.utils.colors.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.colors.Transparent;
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
            Context context = mTextView.getContext();

            Drawable transparentIcon = new TintedDrawable(context,
                    R.drawable.schedjoules_ic_location_on_black_24dp,
                    Transparent.INSTANCE).get();
            mTextView.setCompoundDrawablesWithIntrinsicBounds(transparentIcon, null, null, null);

            Drawable icon = new TintedDrawable(context,
                    R.drawable.schedjoules_ic_location_on_black_24dp,
                    new AttributeColor(context, R.attr.colorAccent)).get();
            mIconAnchorTextView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        }
    }

}
