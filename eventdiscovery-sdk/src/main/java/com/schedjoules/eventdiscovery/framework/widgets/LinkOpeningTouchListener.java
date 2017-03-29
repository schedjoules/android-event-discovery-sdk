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

import android.text.Layout;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * A {@link View.OnTouchListener} that can act similarly as {@link LinkMovementMethod} for a {@link TextView},
 * so with the difference that it doesn't scroll the view.
 * <p>
 * See <a href="http://stackoverflow.com/questions/14579785">Can I disable the scrolling in TextView when using LinkMovementMethod?</a>
 */
public class LinkOpeningTouchListener implements View.OnTouchListener
{

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        TextView widget = (TextView) v;
        Object text = widget.getText();
        if (text instanceof Spanned)
        {
            Spanned buffer = (Spanned) text;

            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_DOWN)
            {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off,
                        ClickableSpan.class);

                if (link.length != 0)
                {
                    if (action == MotionEvent.ACTION_UP)
                    {
                        link[0].onClick(widget);
                    }
                    else if (action == MotionEvent.ACTION_DOWN)
                    {
                        // Selection only works on Spannable text. In our case setSelection doesn't work on spanned text
                        //Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }
                    return true;
                }
            }

        }

        return false;
    }

}