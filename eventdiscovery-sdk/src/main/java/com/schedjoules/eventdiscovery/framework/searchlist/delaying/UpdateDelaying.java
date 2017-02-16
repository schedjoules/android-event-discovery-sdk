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

package com.schedjoules.eventdiscovery.framework.searchlist.delaying;

import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.changes.notifying.ListChange;
import com.schedjoules.eventdiscovery.framework.list.sectioned.SectionableListItem;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchListItems;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdate;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.SectionedResultUpdateListener;


/**
 * Decorator for {@link SearchListItems} that delays applying the {@link ResultUpdate} when need to not 'overlap' animations.
 *
 * @author Gabor Keszthelyi
 */
public final class UpdateDelaying implements SearchListItems
{
    private final SearchListItems mDelegate;
    private final SectionedResultUpdateListener<ListItem> mDelayingListener;


    /**
     * @param waitMillis
     *         the time while a new incoming update will be waited for others to arrive before firing them together
     * @param delayMillis
     *         the minimum time between fired updates, to let the previous animation finish
     */
    public UpdateDelaying(SearchListItems delegate, long waitMillis, long delayMillis)
    {
        mDelegate = delegate;
        mDelayingListener = new DelayingUpdateListener(delegate, waitMillis, delayMillis);
    }


    @Override
    public void onSearchQueryChange(String newQuery)
    {
        mDelegate.onSearchQueryChange(newQuery);
    }


    @Override
    public ListItem get(int position)
    {
        return mDelegate.get(position);
    }


    @Override
    public int itemCount()
    {
        return mDelegate.itemCount();
    }


    @Override
    @MainThread
    public void apply(ListChange<SectionableListItem> listChange)
    {
        mDelegate.apply(listChange);
    }


    @Override
    public void setAdapter(RecyclerView.Adapter adapter)
    {
        mDelegate.setAdapter(adapter);
    }


    @Override
    public void onUpdate(int sectionNumber, ResultUpdate<ListItem> sectionUpdate)
    {
        mDelayingListener.onUpdate(sectionNumber, sectionUpdate);
    }

}
