/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.eventdetails;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.actions.Action;
import com.schedjoules.eventdiscovery.actions.ActionViewIterable;


/**
 * {@link View} for the items on the event actions top horizontal bar.
 *
 * @author Gabor Keszthelyi
 */
public final class SmallEventActionView extends LinearLayout
{

    private ImageView mIcon;
    private TextView mName;


    public final static class Factory implements ActionViewIterable.ActionViewFactory
    {
        private final ViewGroup mParent;


        public Factory(@NonNull ViewGroup parent)
        {
            this.mParent = parent;
        }


        @NonNull
        @Override
        public View actionView(@NonNull Action action)
        {
            SmallEventActionView view = SmallEventActionView.inflate(mParent);
            view.setAction(action);
            return view;
        }
    }


    public static SmallEventActionView inflate(ViewGroup parent)
    {
        return (SmallEventActionView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedjoules_event_action_horizontal, parent, false);
    }


    public SmallEventActionView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        mIcon = (ImageView) findViewById(R.id.schedjoules_small_event_action_icon);
        mName = (TextView) findViewById(R.id.schedjoules_small_event_action_name);
    }


    private void setAction(final Action action)
    {
        Context context = getContext();
        mName.setText(action.shortLabel(context));
        mIcon.setImageDrawable(action.icon(context));
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                action.actionExecutable().execute((Activity) getContext());
            }
        });
    }
}
