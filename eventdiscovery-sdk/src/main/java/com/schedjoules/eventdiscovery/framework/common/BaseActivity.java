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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.commons.IntentBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalBoxArgument;
import com.schedjoules.eventdiscovery.framework.services.BasicActionsService;
import com.schedjoules.eventdiscovery.framework.services.BasicEventService;
import com.schedjoules.eventdiscovery.framework.services.BasicInsightsService;

import org.dmfs.optional.Optional;


/**
 * Base class for all Activities in the app.
 *
 * @author Gabor Keszthelyi
 */
public abstract class BaseActivity extends AppCompatActivity
{

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Optional<Integer> themeExtra = new OptionalArgument<>(Keys.THEME, getIntent());
        if (themeExtra.isPresent())
        {
            setTheme(themeExtra.value());
        }
        // Start the services if not started yet. No need to stop them manually, they will stop automatically.
        BasicInsightsService.start(this);
        BasicActionsService.start(this);
        BasicEventService.start(this);
    }


    @Override
    public final void startActivity(Intent intent)
    {
        // make sure to launch the activity with our custom theme
        super.startActivity(withTheme(intent));
    }


    @Override
    public final void startActivityForResult(Intent intent, int requestCode)
    {
        // make sure to launch the activity with our custom theme
        super.startActivityForResult(withTheme(intent), requestCode);
    }


    @Override
    public final void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode)
    {
        // make sure to launch the activity with our custom theme
        super.startActivityFromFragment(fragment, withTheme(intent), requestCode);
    }


    @Override
    public final void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options)
    {
        // make sure to launch the activity with our custom theme
        super.startActivityFromFragment(fragment, withTheme(intent), requestCode, options);
    }


    private Intent withTheme(Intent intent)
    {
        return new IntentBuilder(intent).with(Keys.THEME, new OptionalBoxArgument<>(Keys.THEME, getIntent())).build();
    }
}
