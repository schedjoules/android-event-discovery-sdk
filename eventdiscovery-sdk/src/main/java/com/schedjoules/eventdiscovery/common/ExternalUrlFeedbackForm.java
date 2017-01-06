/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;


/**
 * @author Gabor Keszthelyi
 */
public final class ExternalUrlFeedbackForm implements FeedbackForm
{
    @Override
    public void show(Activity activity)
    {
        if (activity != null)
        {
            Intent feedbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/forms/IcGLVceoRvG2F1Fe2"));
            activity.startActivity(feedbackIntent);
        }
    }
}
