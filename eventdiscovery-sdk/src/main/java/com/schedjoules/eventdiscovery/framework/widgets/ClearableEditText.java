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

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;

import com.schedjoules.eventdiscovery.R;


/**
 * {@link EditText} that has an 'X' right side compound drawable as a clear button.
 *
 * @author Gabor Keszthelyi
 */
public final class ClearableEditText extends EditText
{

    private GestureDetectorCompat mGestureDetector;


    public ClearableEditText(Context context)
    {
        super(context);
    }


    public ClearableEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        addTextChangedListener(new AbstractTextWatcher()
        {
            @Override
            public void afterTextChanged(Editable text)
            {
                int clearIcon = text.length() > 0 ? R.drawable.schedjoules_ic_clear : 0;
                setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0);
            }
        });

        mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent event)
            {
                if (event.getRawX() >= getRight() - getTotalPaddingRight())
                {
                    setText("");
                }
                return false;
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
