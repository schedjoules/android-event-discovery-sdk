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

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.equalables.LazyObjectEqualable;
import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;


/**
 * A {@link ListItem} on the location picker that displays a message and a button for an action.
 *
 * @author Gabor Keszthelyi
 */
public final class ActionMessageItem implements ListItem<ActionMessageItemView>
{

    private final CharSequence mMessage;
    private final CharSequence mActionLabel;
    private final OnClickAction mAction;
    private final Equalable mId;


    public ActionMessageItem(final CharSequence message, final CharSequence actionLabel, OnClickAction action)
    {
        mMessage = message;
        mActionLabel = actionLabel;
        mAction = action;

        mId = new LazyObjectEqualable(new Factory<Object>()
        {
            @Override
            public Object create()
            {
                return message.toString() + "|" + actionLabel.toString();
            }
        });
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_location_action_message;
    }


    @Override
    public void bindDataTo(ActionMessageItemView view)
    {
        view.update(mMessage);

        Button button = (Button) view.findViewById(ActionMessageItemView.ID_ACTION_BUTTON);
        button.setText(mActionLabel);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAction.onClick();
            }
        });
    }


    @NonNull
    @Override
    public Equalable id()
    {
        return mId;
    }

}
