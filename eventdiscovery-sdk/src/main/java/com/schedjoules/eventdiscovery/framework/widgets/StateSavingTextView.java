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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * {@link TextView} that saves and restores its state, i.e. text and visibility, on configuration change.
 * <p>
 * See http://stackoverflow.com/a/18679823.
 *
 * @author Gabor Keszthelyi
 */
public final class StateSavingTextView extends TextView
{
    private static final String SUPER_STATE = "super_state";
    private static final String ALPHA = "alpha";
    private static final String VISIBILITY = "visibility";


    public StateSavingTextView(Context context)
    {
        super(context);
        init();
    }


    public StateSavingTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }


    public StateSavingTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StateSavingTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init()
    {
        setFreezesText(true);
    }


    @Override
    public Parcelable onSaveInstanceState()
    {
        Bundle state = new Bundle();

        //Piggyback off of the View's implementation and store that
        //bundle of saved information in our container bundle
        state.putParcelable(SUPER_STATE, super.onSaveInstanceState());

        //Store the current visibility of the View in the saved state
        state.putInt(VISIBILITY, getVisibility());
        state.putFloat(ALPHA, getAlpha());
        return state;
    }


    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        //state should always be an instance of Bundle since that's what
        //we're saving, but check for safety
        if (state instanceof Bundle)
        {
            Bundle savedState = (Bundle) state;

            //Set the visibility of the View to match the visibility that
            //we retained in onSavedInstanceState(), falling back to the
            //current visibility as default if no state was saved
            //noinspection WrongConstant
            setVisibility(savedState.getInt(VISIBILITY, getVisibility()));

            setAlpha(savedState.getFloat(ALPHA, getAlpha()));
            //Pull out the superclass state we saved, and let the superclass
            //handle restoring all of the other state
            Parcelable superState = savedState.getParcelable(SUPER_STATE);
            super.onRestoreInstanceState(superState);
        }
        else
        {
            //Nothing special to do here other than pass it up to the super
            super.onRestoreInstanceState(state);
        }
    }
}
