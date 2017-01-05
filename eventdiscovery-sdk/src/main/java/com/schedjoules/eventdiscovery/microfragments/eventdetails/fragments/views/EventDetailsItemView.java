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

package com.schedjoules.eventdiscovery.microfragments.eventdetails.fragments.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.actions.Action;
import com.schedjoules.eventdiscovery.actions.ActionViewIterable;


/**
 * {@link View} for the event detail items listed vertically on the details screen.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsItemView extends RelativeLayout
{
    private ImageView mIcon;
    private TextView mTitle;


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
            EventDetailsItemView view = EventDetailsItemView.inflate(mParent);
            view.setAction(action);
            return view;
        }
    }


    public static EventDetailsItemView inflate(ViewGroup parent)
    {
        return (EventDetailsItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedjoules_view_event_details_item, parent, false);
        /*
         * TODO The usage of parent in inflate() is actually not needed, maybe even incorrect after reading the docs.
          * So it could be removed here (use null) and elsewhere in the code base as well.
         */
    }


    public EventDetailsItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mIcon = (ImageView) findViewById(R.id.schedjoules_event_details_item_icon);
        mTitle = (TextView) findViewById(R.id.schedjoules_event_details_item_title);
    }


    public void setTextAsTitle(CharSequence text)
    {
        mTitle.setText(text);
    }


    public void setIcon(@DrawableRes int iconResId)
    {
        mIcon.setImageResource(iconResId);
    }


    private void setAction(final Action action)
    {
        Context context = getContext();
        mTitle.setText(action.longLabel(context));
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
