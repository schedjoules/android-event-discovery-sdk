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

package com.schedjoules.eventdiscovery.framework.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentState;
import org.dmfs.android.microfragments.SimpleMicroFragmentFlow;
import org.dmfs.pigeonpost.Dovecote;
import org.dmfs.pigeonpost.localbroadcast.ParcelableDovecote;


/**
 * An activity presenting the given {@link MicroFragment}.
 * <p>
 * This is a copy of {@link MicroFragmentHostActivity} that listens for clicks on the up button in order to close the activity.
 * <p>
 * To be removed when https://github.com/schedjoules/android-event-discovery-sdk/issues/72 has been implemented
 *
 * @author Marten Gajda
 */
public final class FeedbackMicroFragmentHostActivity extends BaseActivity implements Dovecote.OnPigeonReturnCallback<MicroFragmentState>
{
    private Dovecote<MicroFragmentState> mMicroFragmentStateDovecote;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedjoules_activity_frame);
        mMicroFragmentStateDovecote = new ParcelableDovecote<>(this, "microfragmentstate", this);

        if (savedInstanceState == null)
        {
            // load the initial MicroFragment
            Bundle nestedExtras = getIntent().getBundleExtra("com.schedjoules.nestedExtras");
            MicroFragment initialMicroFragment = nestedExtras.getParcelable("MicroFragment");
            new SimpleMicroFragmentFlow(initialMicroFragment, R.id.schedjoules_activity_content).withPigeonCage(mMicroFragmentStateDovecote.cage()).start(this);
        }
    }


    @Override
    protected void onDestroy()
    {
        mMicroFragmentStateDovecote.dispose();
        super.onDestroy();
    }


    @Override
    public void onPigeonReturn(@NonNull MicroFragmentState microFragmentState)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            // override up button behavior and close the activity
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
        }
    }
}
