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
import android.support.v7.widget.LinearLayoutManager;

import com.schedjoules.eventdiscovery.framework.eventlist.items.DateHeaderItem;
import com.schedjoules.eventdiscovery.framework.list.flexibleadapter.FlexibleHeaderItemAdapter;
import com.schedjoules.eventdiscovery.framework.utils.Objects;

import org.dmfs.rfc5545.DateTime;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;

import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.BOTTOM;
import static com.schedjoules.eventdiscovery.framework.eventlist.controller.ScrollDirection.TOP;


/**
 * Implementation for {@link EventListItems}.
 *
 * @author Gabor Keszthelyi
 */
public final class FlexibleAdapterEventListItems implements EventListItems<IFlexible, FlexibleAdapter<IFlexible>>
{

    private final Handler mainHandler;
    private final IHeader mTodayHeader;
    private FlexibleAdapter<IFlexible> mFlexibleAdapter;


    public FlexibleAdapterEventListItems()
    {
        mainHandler = new Handler(Looper.getMainLooper());

        // TODO: might not be always correct if screen is kept on for long, check if day passed trigger solved this
        mTodayHeader = new FlexibleHeaderItemAdapter<>(new DateHeaderItem(DateTime.now()));
    }


    @Override
    public void setAdapter(FlexibleAdapter<IFlexible> flexibleAdapter)
    {
        mFlexibleAdapter = flexibleAdapter;
    }


    @Override
    public void mergeNewItems(List<IFlexible> newItems, ScrollDirection direction)
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
    public void addSpecialItemPost(final IFlexible specialItem, final ScrollDirection direction)
    {
        // Posting is needed otherwise it can get added in the same loop, resulting in pushing the list down
        mainHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                addSpecialItemNow(specialItem, direction);
            }
        }, 1);
    }


    @Override
    public void addSpecialItemNow(IFlexible specialItem, ScrollDirection direction)
    {
        int position = direction == TOP ? 0 : Integer.MAX_VALUE;
        mFlexibleAdapter.addItem(position, specialItem);
    }


    @Override
    public void removeSpecialItem(IFlexible specialItem, ScrollDirection direction)
    {
        int position = mFlexibleAdapter.getGlobalPositionOf(specialItem);
        if (position >= 0)
        {
            mFlexibleAdapter.removeItem(position);
        }
    }


    @Override
    public boolean isTodayShown()
    {
        return mFlexibleAdapter.contains(mTodayHeader);
    }


    @Override
    public boolean isEmpty()
    {
        return mFlexibleAdapter.isEmpty();
    }


    @Override
    public void clear()
    {
        mFlexibleAdapter.clear();
    }


    private void mergeNewItemsBottom(List<IFlexible> newItems)
    {
        IHeader lastDayInCurrent = getLastHeaderCurrent();
        IHeader firstDayInNew = getFirstHeader(newItems);

        if (Objects.equals(lastDayInCurrent, firstDayInNew))
        {

            for (IFlexible newItem : newItems)
            {
                if (newItem instanceof ISectionable)
                {
                    ISectionable sectionable = ((ISectionable) newItem);
                    if (sectionable.getHeader().equals(firstDayInNew))
                    {
                        sectionable.setHeader(lastDayInCurrent);
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }

        mFlexibleAdapter.addItems(Integer.MAX_VALUE, newItems);
    }


    private void mergeNewItemsTop(List<IFlexible> newItems)
    {
        IHeader firstDayInCurrent = getFirstHeaderCurrent();
        IHeader lastDayInNew = getLastHeader(newItems);

        /*
         * TODO Workaround logic below, could potentially be removed
         * See more: https://github.com/schedjoules/android-event-discovery-sdk/issues/335
         */
        if (Objects.equals(lastDayInNew, firstDayInCurrent))
        {
            mFlexibleAdapter.removeItem(mFlexibleAdapter.getGlobalPositionOf(firstDayInCurrent));
            int lastItemWithDistinctHeader = -1;
            for (int i = newItems.size() - 1; i >= 0; i--)
            {
                if (newItems.get(i) instanceof ISectionable)
                {
                    ISectionable item = ((ISectionable) newItems.get(i));
                    if (item.getHeader().equals(lastDayInNew))
                    {
                        item.setHeader(firstDayInCurrent);
                        mFlexibleAdapter.addItemToSection(item, firstDayInCurrent, 0);
                    }
                    else
                    {
                        lastItemWithDistinctHeader = i;
                        break;
                    }
                }
            }
            List<IFlexible> distinctItems = newItems.subList(0, ++lastItemWithDistinctHeader);
            mFlexibleAdapter.addItems(0, distinctItems);
        }
        else
        {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mFlexibleAdapter.getRecyclerView().getLayoutManager();
            int visibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (visibleItemPosition == 0)
            {
                mFlexibleAdapter.setStickyHeaders(false);
                mFlexibleAdapter.addItems(0, newItems);
                mFlexibleAdapter.setStickyHeaders(true);
            }
            else
            {
                mFlexibleAdapter.removeItem(mFlexibleAdapter.getGlobalPositionOf(firstDayInCurrent));

                ISectionable dummyItem = (ISectionable) newItems.get(newItems.size() - 1);
                mFlexibleAdapter.addItemToSection(dummyItem, firstDayInCurrent, 0);
                dummyItem.setHeader(lastDayInNew);

                mFlexibleAdapter.addItems(0, newItems);

                mFlexibleAdapter.removeItem(mFlexibleAdapter.getGlobalPositionOf(dummyItem) + 2);
            }
        }

    }


    private IHeader getFirstHeaderCurrent()
    {
        List<IHeader> headerItems = mFlexibleAdapter.getHeaderItems();
        return headerItems.isEmpty() ? null : headerItems.get(0);
    }


    private IHeader getLastHeaderCurrent()
    {
        List<IHeader> headerItems = mFlexibleAdapter.getHeaderItems();
        return headerItems.isEmpty() ? null : headerItems.get(headerItems.size() - 1);
    }


    private IHeader getLastHeader(List<IFlexible> newItems)
    {
        for (int i = newItems.size() - 1; i >= 0; i--)
        {
            if (newItems.get(i) instanceof ISectionable)
            {
                return ((ISectionable) newItems.get(i)).getHeader();
            }
        }
        return null;
    }


    private IHeader getFirstHeader(List<IFlexible> newItems)
    {
        for (int i = 0; i < newItems.size(); i++)
        {
            if (newItems.get(i) instanceof ISectionable)
            {
                return ((ISectionable) newItems.get(i)).getHeader();
            }
        }
        return null;
    }

}


