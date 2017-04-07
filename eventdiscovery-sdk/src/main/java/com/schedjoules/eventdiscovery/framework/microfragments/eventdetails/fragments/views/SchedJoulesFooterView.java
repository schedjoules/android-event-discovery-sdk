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
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;


/**
 * View for the SchedJoules footer.
 *
 * @author Gabor Keszthelyi
 */
public final class SchedJoulesFooterView extends AppCompatTextView implements View.OnClickListener
{
    public SchedJoulesFooterView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.schedjoules.com?utm_source=" + v.getContext().getPackageName()));

        if (intent.resolveActivity(getContext().getPackageManager()) != null)
        {
            getContext().startActivity(intent);
        }
    }
}
