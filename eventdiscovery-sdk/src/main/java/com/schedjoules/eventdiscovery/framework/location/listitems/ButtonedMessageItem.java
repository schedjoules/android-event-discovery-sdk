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

package com.schedjoules.eventdiscovery.framework.location.listitems;

import android.view.View;
import android.widget.Button;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;


/**
 * A {@link ListItem} on the location picker that displays a message and a button.
 *
 * @author Gabor Keszthelyi
 */
public final class ButtonedMessageItem implements ListItem<ButtonedMessageItemView>
{

    private final CharSequence mMessageText;
    private final CharSequence mButtonText;
    private final OnClickAction mButtonClickAction;


    public ButtonedMessageItem(CharSequence messageText, CharSequence buttonText, OnClickAction buttonClickAction)
    {
        mMessageText = messageText;
        mButtonText = buttonText;
        mButtonClickAction = buttonClickAction;
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_location_buttoned_message;
    }


    @Override
    public void bindDataTo(ButtonedMessageItemView view)
    {
        view.update(mMessageText);

        Button button = (Button) view.findViewById(ButtonedMessageItemView.ID_BUTTON);
        button.setText(mButtonText);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mButtonClickAction.onClick();
            }
        });
    }
}
