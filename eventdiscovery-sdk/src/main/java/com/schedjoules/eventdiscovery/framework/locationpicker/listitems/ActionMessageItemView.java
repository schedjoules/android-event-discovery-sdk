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

package com.schedjoules.eventdiscovery.framework.locationpicker.listitems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;


/**
 * A {@link View} for a location picker list item that shows a message and a button for an action.
 *
 * @author Gabor Keszthelyi
 */
public final class ActionMessageItemView extends RelativeLayout implements SmartView<CharSequence>
{
    public static int ID_ACTION_BUTTON = R.id.schedjoules_place_message_item_button;

    private TextView mTextView;


    public ActionMessageItemView(Context context)
    {
        super(context);
    }


    public ActionMessageItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mTextView = (TextView) findViewById(R.id.schedjoules_place_message_item_title);
    }


    @Override
    public void update(CharSequence text)
    {
        mTextView.setText(text);
    }

}
