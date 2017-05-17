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

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.SimpleMicroFragmentFlow;
import org.dmfs.android.microfragments.transitions.BackTransition;
import org.dmfs.android.microfragments.utils.BooleanDovecote;
import org.dmfs.pigeonpost.Dovecote;


/**
 * An activity presenting the given {@link MicroFragment}.
 *
 * @author Marten Gajda
 */
public final class MicroFragmentHostActivity extends BaseActivity
{
    private Dovecote<Boolean> mBackDovecote;
    private MicroFragmentHost mMicroFragmentHost;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedjoules_activity_frame);

        // the BackDovecote receives Pigeons with the result of the BackTransition.
        mBackDovecote = new BooleanDovecote(this, "backresult", new Dovecote.OnPigeonReturnCallback<Boolean>()
        {
            @Override
            public void onPigeonReturn(@NonNull Boolean wentback)
            {
                if (!wentback)
                {
                    // there was not other MicroFragment in the stack, so close this activity.
                    finish();
                }
            }
        });

        if (savedInstanceState == null)
        {
            // load the initial MicroFragment
            Bundle nestedExtras = getIntent().getBundleExtra("com.schedjoules.nestedExtras");
            MicroFragment initialMicroFragment = nestedExtras.getParcelable("MicroFragment");
            mMicroFragmentHost = new SimpleMicroFragmentFlow(initialMicroFragment, R.id.schedjoules_activity_content).start(this);
        }
        else
        {
            mMicroFragmentHost = savedInstanceState.getParcelable("host");
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // retain the MicroFragmentHost because we still need it after a configuration change (to fire BackTransitions)
        outState.putParcelable("host", mMicroFragmentHost);
    }


    @Override
    protected void onDestroy()
    {
        mBackDovecote.dispose();
        super.onDestroy();
    }


    @Override
    public void onBackPressed()
    {
        mMicroFragmentHost.execute(this, new BackTransition(mBackDovecote.cage()));
    }
}
