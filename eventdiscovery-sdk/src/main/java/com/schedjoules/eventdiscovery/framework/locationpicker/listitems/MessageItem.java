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

import android.support.annotation.NonNull;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.equalables.LazyToStringEqualable;


/**
 * A {@link ListItem} on the location picker that displays a message.
 *
 * @author Gabor Keszthelyi
 */
public final class MessageItem implements ListItem<MessageItemView>
{

    private final CharSequence mText;
    private final Equalable mId;


    public MessageItem(CharSequence text)
    {
        mText = text;
        mId = new LazyToStringEqualable(text);
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_location_message;
    }


    @Override
    public void bindDataTo(MessageItemView view)
    {
        view.update(mText);
    }


    @NonNull
    @Override
    public Equalable id()
    {
        return mId;
    }


    @Override
    public String toString()
    {
        return "MessageItem{" +
                "mText=" + mText +
                '}';
    }
}
