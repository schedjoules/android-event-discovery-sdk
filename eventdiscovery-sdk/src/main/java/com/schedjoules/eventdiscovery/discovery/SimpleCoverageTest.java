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

package com.schedjoules.eventdiscovery.discovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.schedjoules.eventdiscovery.framework.services.CoverageService;

import org.dmfs.pigeonpost.Dovecote;

import java.io.Serializable;


/**
 * A simple {@link CoverageTest} implementation.
 *
 * @author Marten Gajda
 */
public final class SimpleCoverageTest implements CoverageTest
{

    private final Dovecote<Boolean> mDoveCote;


    public SimpleCoverageTest(Dovecote<Boolean> doveCote)
    {
        mDoveCote = doveCote;
    }


    @Override
    public void execute(Context context)
    {
        Bundle data = new Bundle();
        data.putParcelable(CoverageService.KEY_PIGEONCAGE, mDoveCote.cage());
        context.startService(new Intent(context, CoverageService.class).putExtra(CoverageService.EXTRA_NESTED_DATA, data));
    }
}
