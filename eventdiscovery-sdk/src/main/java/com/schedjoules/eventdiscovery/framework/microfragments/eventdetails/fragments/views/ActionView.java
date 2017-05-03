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
import android.view.View;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewEventDetailsActionBinding;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesViewEventDetailsActionDirectionsBinding;
import com.schedjoules.eventdiscovery.framework.actions.Action;
import com.schedjoules.eventdiscovery.framework.actions.ActionClickListener;
import com.schedjoules.eventdiscovery.framework.utils.AttributeColor;
import com.schedjoules.eventdiscovery.framework.utils.TintedDrawable;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;

import org.dmfs.optional.Optional;


/**
 * Represents the View for an Event Action on the details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class ActionView implements SmartView<Optional<Action>>
{
    private final View mRoot;
    private final TextView mTextView;


    private ActionView(View root, TextView textView)
    {
        mRoot = root;
        mTextView = textView;
    }


    public ActionView(SchedjoulesViewEventDetailsActionBinding binding)
    {
        this(binding.getRoot(), binding.schedjoulesEventDetailsActionLabel);
    }


    public ActionView(SchedjoulesViewEventDetailsActionDirectionsBinding directionsBinding)
    {
        this(directionsBinding.getRoot(), directionsBinding.schedjoulesEventDetailsDirectionsLabel);
    }


    @Override
    public void update(Optional<Action> optAction)
    {
        if (optAction.isPresent())
        {
            final Action action = optAction.value();

            Context context = mTextView.getContext();

            mTextView.setText(action.label(context));

            Drawable icon = TintedDrawable.tinted(action.icon(context), new AttributeColor(context, R.attr.colorAccent));
            mTextView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

            mRoot.setOnClickListener(new ActionClickListener(action.actionExecutable()));
            mRoot.setVisibility(View.VISIBLE);
        }
        else
        {
            mRoot.setVisibility(View.GONE);
        }
    }
}
