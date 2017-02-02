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

package com.schedjoules.eventdiscovery.framework.eventlist.controller;

import android.os.Handler;
import android.os.Looper;

import com.android.annotations.VisibleForTesting;
import com.schedjoules.eventdiscovery.framework.eventlist.items.DateHeaderItem;
import com.schedjoules.eventdiscovery.framework.eventlist.items.EventItem;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.flexibleadapter.ThirdPartyAdapterNotifier;
import com.schedjoules.eventdiscovery.framework.utils.Objects;

import org.dmfs.rfc5545.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.TOP;


/**
 * Implementation for {@link EventListItems}.
 *
 * @author Gabor Keszthelyi
 */
public final class EventListItemsImpl implements EventListItems
{
    private final List<ListItem> mItems = new ArrayList<>();

    private final Handler mMainHandler;

    private ThirdPartyAdapterNotifier mAdapterNotifier;


    public EventListItemsImpl()
    {
        this(new Handler(Looper.getMainLooper()));
    }


    @VisibleForTesting
    EventListItemsImpl(Handler mainHandler)
    {
        mMainHandler = mainHandler;
    }


    @Override
    public void setAdapterNotifier(ThirdPartyAdapterNotifier adapterNotifier)
    {
        mAdapterNotifier = adapterNotifier;
        // Needed after rotation for FlexibleAdapter
        mAdapterNotifier.notifyInitialItemsAdded(mItems);
    }


    @Override
    public void mergeNewItems(List<ListItem> newItems, ScrollDirection direction)
    {
        if (newItems.isEmpty())
        {
            return;
        }

        if (direction == BOTTOM)
        {
            mergeNewItemsBottom(newItems);
        }
        else
        {
            mergeNewItemsTop(newItems);
        }
    }


    @Override
    public void addSpecialItemPost(final ListItem specialItem, final ScrollDirection direction)
    {
        // Posting is needed otherwise it can get added in the same loop, resulting in pushing the list down
        mMainHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                addSpecialItemNow(specialItem, direction);
            }
        }, 1);
    }


    @Override
    public void addSpecialItemNow(ListItem specialItem, ScrollDirection direction)
    {
        int position = direction == TOP ? 0 : mItems.size();
        mItems.add(position, specialItem);
        //noinspection unchecked
        mAdapterNotifier.notifyNewItemAdded(specialItem, position);
    }


    @Override
    public void removeSpecialItem(ListItem specialItem, ScrollDirection direction)
    {
        int index = indexOfItem(specialItem, direction);
        if (index >= 0)
        {
            mItems.remove(index);
            mAdapterNotifier.notifyItemRemoved(index);
        }
    }


    @Override
    public boolean isTodayShown()
    {
        return indexOfItem(new DateHeaderItem(DateTime.now()), TOP) >= 0;
    }


    private int indexOfItem(ListItem item, ScrollDirection direction)
    {
        return direction == TOP ? mItems.indexOf(item) : mItems.lastIndexOf(item);
    }


    @Override
    public boolean isEmpty()
    {
        return mItems.isEmpty();
    }


    @Override
    public void clear()
    {
        int size = mItems.size();
        mItems.clear();
        mAdapterNotifier.notifyItemsCleared(size);
    }


    private void mergeNewItemsBottom(List<ListItem> newItems)
    {
        DateHeaderItem lastDayInCurrent = getLastDateHeader(mItems);
        DateHeaderItem firstDayInNew = getFirstDateHeader(newItems);

        if (Objects.equals(lastDayInCurrent, firstDayInNew))
        {
            newItems.remove(firstDayInNew);
            for (ListItem newItem : newItems)
            {
                if (newItem instanceof EventItem)
                {
                    EventItem eventItem = ((EventItem) newItem);
                    if (eventItem.getHeader().equals(firstDayInNew))
                    {
                        eventItem.setHeader(lastDayInCurrent);
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }

        int sizeBefore = mItems.size();
        mItems.addAll(newItems);
        mAdapterNotifier.notifyNewItemsAdded(newItems, sizeBefore);
    }


    private void mergeNewItemsTop(List<ListItem> newItems)
    {
        DateHeaderItem firstDayInCurrent = getFirstDateHeader(mItems);
        DateHeaderItem lastDayInNew = getLastDateHeader(newItems);

        if (Objects.equals(lastDayInNew, firstDayInCurrent))
        {
            int index = mItems.indexOf(firstDayInCurrent);
            mItems.remove(firstDayInCurrent);
            mAdapterNotifier.notifyItemRemoved(index);

            replace(newItems, lastDayInNew, firstDayInCurrent);

            for (int i = newItems.size() - 1; i >= 0; i--)
            {
                if (newItems.get(i) instanceof EventItem)
                {
                    EventItem eventItem = ((EventItem) newItems.get(i));
                    if (eventItem.getHeader().equals(lastDayInNew))
                    {
                        eventItem.setHeader(firstDayInCurrent);
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }

        mItems.addAll(0, newItems);
        mAdapterNotifier.notifyNewItemsAdded(newItems, 0);
    }


    private void replace(List<ListItem> newItems, DateHeaderItem lastDayInNew, DateHeaderItem firstDayInCurrent)
    {
        int index = newItems.indexOf(lastDayInNew);
        newItems.remove(index);
        newItems.add(index, firstDayInCurrent);
    }


    private DateHeaderItem getLastDateHeader(List<ListItem> items)
    {
        ListIterator<ListItem> listIterator = items.listIterator(items.size());
        while (listIterator.hasPrevious())
        {
            ListItem previous = listIterator.previous();
            if (previous instanceof DateHeaderItem)
            {
                return (DateHeaderItem) previous;
            }
        }
        return null;
    }


    private DateHeaderItem getFirstDateHeader(List<ListItem> items)
    {
        for (ListItem item : items)
        {
            if (item instanceof DateHeaderItem)
            {
                return (DateHeaderItem) item;
            }
        }
        return null;
    }


    @Override
    public ListItem get(int position)
    {
        return mItems.get(position);
    }


    @Override
    public int itemCount()
    {
        return mItems.size();
    }
}
