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

/**
 * Adapts {@link ResultUpdateListener} to {@link SectionedResultUpdateListener}.
 *
 * @author Gabor Keszthelyi
 */
public final class SectionedResultUpdateListenerAdapter<T> implements ResultUpdateListener<T>
{
    private final int mSectionNumber;
    private final SectionedResultUpdateListener<T> mSectionedUpdateListener;


    public SectionedResultUpdateListenerAdapter(int sectionNumber, SectionedResultUpdateListener<T> sectionedUpdateListener)
    {
        mSectionNumber = sectionNumber;
        mSectionedUpdateListener = sectionedUpdateListener;
    }


    @Override
    public void onUpdate(ResultUpdate<T> update)
    {
        mSectionedUpdateListener.onUpdate(mSectionNumber, update);
    }
}
