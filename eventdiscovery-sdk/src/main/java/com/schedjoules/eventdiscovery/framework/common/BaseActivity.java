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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.schedjoules.eventdiscovery.framework.serialization.Keys;
import com.schedjoules.eventdiscovery.framework.serialization.SchedJoulesKey;
import com.schedjoules.eventdiscovery.framework.serialization.boxes.ParcelableBox;
import com.schedjoules.eventdiscovery.framework.serialization.commons.FluentBundle;
import com.schedjoules.eventdiscovery.framework.serialization.commons.IntentBuilder;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalArgument;
import com.schedjoules.eventdiscovery.framework.serialization.commons.OptionalBoxArgument;
import com.schedjoules.eventdiscovery.framework.serialization.core.Key;
import com.schedjoules.eventdiscovery.framework.services.BasicActionsService;
import com.schedjoules.eventdiscovery.framework.services.BasicEventService;
import com.schedjoules.eventdiscovery.framework.services.BasicInsightsService;

import org.dmfs.optional.NullSafe;
import org.dmfs.optional.Optional;


/**
 * Base class for all Activities in the app.
 *
 * @author Gabor Keszthelyi
 */
public abstract class BaseActivity extends AppCompatActivity
{

    public static final String SERVICE_ACTIVITY = "schedjoules.activity";

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private final static Key<Bundle> KEY_CONTEXT_STATE = new SchedJoulesKey<>("CONTEXT_STATE");
    private Bundle mContextState;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContextState = new OptionalArgument<>(KEY_CONTEXT_STATE, new NullSafe<>(savedInstanceState)).value(new Bundle());
        mContextState.setClassLoader(getClassLoader());

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
    protected void onSaveInstanceState(Bundle outState)
    {
        new FluentBundle(outState).put(KEY_CONTEXT_STATE, new ParcelableBox<>(mContextState));
        super.onSaveInstanceState(outState);
    }


    @Override
    public Object getSystemService(@NonNull String name)
    {
        if (name.equals(SERVICE_ACTIVITY))
        {
            return this;
        }
        else if (name.equals(ContextState.SERVICE_CONTEXT_STATE))
        {
            return mContextState;
        }
        return super.getSystemService(name);
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
