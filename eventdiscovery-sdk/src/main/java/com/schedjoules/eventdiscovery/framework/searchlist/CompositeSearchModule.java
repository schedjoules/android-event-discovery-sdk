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

package com.schedjoules.eventdiscovery.framework.searchlist;

import java.util.List;


/**
 * {@link SearchModule} that dispatches the calls to the provided list of {@link SearchModule}s.
 *
 * @author Gabor Keszthelyi
 */
public final class CompositeSearchModule implements SearchModule
{
    private final List<SearchModule> mModules;


    public CompositeSearchModule(List<SearchModule> modules)
    {
        mModules = modules;
    }


    @Override
    public void shutDown()
    {
        for (SearchModule module : mModules)
        {
            module.shutDown();
        }
    }


    @Override
    public void onSearchQueryChange(String newQuery)
    {
        for (SearchModule module : mModules)
        {
            module.onSearchQueryChange(newQuery);
        }
    }
}
