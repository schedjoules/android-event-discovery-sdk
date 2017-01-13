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

package com.schedjoules.eventdiscovery.framework.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.schedjoules.eventdiscovery.framework.activities.FeedbackMicroFragmentHostActivity;
import com.schedjoules.eventdiscovery.framework.microfragments.feedback.FeedbackMicroFragment;

import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.transitions.ForwardTransition;
import org.dmfs.android.microfragments.transitions.Swiped;


/**
 * @author Gabor Keszthelyi
 */
public final class ExternalUrlFeedbackForm implements FeedbackForm
{

    @Override
    public void show(Activity activity)
    {
        Intent intent = new Intent(activity, FeedbackMicroFragmentHostActivity.class);
        Bundle nestedBundle = new Bundle(1);
        nestedBundle.putParcelable("MicroFragment", new FeedbackMicroFragment());
        intent.putExtra("com.schedjoules.nestedExtras", nestedBundle);
        activity.startActivity(intent);
    }


    @Override
    public void show(Context context, MicroFragmentHost host)
    {
        host.execute(context, new Swiped(new ForwardTransition(new FeedbackMicroFragment())));
    }
}
