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

package com.schedjoules.eventdiscovery.framework.list.changes;

import com.schedjoules.eventdiscovery.framework.list.AdapterNotifier;
import com.schedjoules.eventdiscovery.framework.list.ListChange;
import com.schedjoules.eventdiscovery.framework.list.ListItem;

import java.util.List;


/**
 * {@link ListChange} that replaces all items with the new ones but keeping common ones while animating appropriately.
 *
 * @author Gabor Keszthelyi
 */
public final class AnimatedReplaceAll implements ListChange
{
    public final List<ListItem> mNewItems;


    public AnimatedReplaceAll(List<ListItem> newItems)
    {
        mNewItems = newItems;
    }


    @Override
    public void apply(List<ListItem> items, AdapterNotifier adapterNotifier)
    {
        updateDataSetWithAnimation(items, mNewItems, adapterNotifier);
    }


    private <T> void updateDataSetWithAnimation(List<T> currentItems, List<T> newItems, AdapterNotifier adapterNotifier)
    {
        if (currentItems.equals(newItems))
        {
            // Same items, doing nothing.
        }
        else if (isOnlyAddition(currentItems, newItems))
        {
            // Addition to the list, no animation.
            addWithoutAnimation(currentItems, newItems, adapterNotifier);
        }
        else
        {
            // Filtering with animations.
            animateChanges(currentItems, newItems, adapterNotifier);
        }
    }


    private <T> boolean isOnlyAddition(List<T> currentItems, List<T> newItems)
    {
        return newItems.size() > currentItems.size() && currentItems.equals(newItems.subList(0, currentItems.size()));
    }


    private <T> void animateChanges(List<T> currentItems, List<T> newItems, AdapterNotifier adapterNotifier)
    {
        applyAndAnimateRemovals(currentItems, newItems, adapterNotifier);
        applyAndAnimateAdditions(currentItems, newItems, adapterNotifier);
        applyAndAnimateMovedItems(currentItems, newItems, adapterNotifier);
    }


    private <T> void addWithoutAnimation(List<T> currentItems, List<T> newItems, AdapterNotifier adapterNotifier)
    {
        currentItems.addAll(newItems.subList(currentItems.size(), newItems.size()));
        adapterNotifier.notifyDataSetChanged();
    }


    private <T> void applyAndAnimateRemovals(List<T> currentItems, List<T> newItems, AdapterNotifier adapterNotifier)
    {
        for (int i = currentItems.size() - 1; i >= 0; i--)
        {
            T aCurrentItem = currentItems.get(i);
            if (!newItems.contains(aCurrentItem))
            {
                removeItem(currentItems, i, adapterNotifier);
            }
        }
    }


    private <T> void applyAndAnimateAdditions(List<T> currentItems, List<T> newItems, AdapterNotifier adapterNotifier)
    {
        for (int i = 0; i < newItems.size(); i++)
        {
            T aNewItem = newItems.get(i);
            if (!currentItems.contains(aNewItem))
            {
                addItem(currentItems, i, aNewItem, adapterNotifier);
            }
        }
    }


    private <T> void applyAndAnimateMovedItems(List<T> currentItems, List<T> newItems, AdapterNotifier adapterNotifier)
    {
        for (int toPosition = newItems.size() - 1; toPosition >= 0; toPosition--)
        {
            T aNewItem = newItems.get(toPosition);
            int fromPosition = currentItems.indexOf(aNewItem);
            if (fromPosition >= 0 && fromPosition != toPosition)
            {
                moveItem(currentItems, fromPosition, toPosition, adapterNotifier);
            }
            else
            {
                adapterNotifier.notifyItemChanged(toPosition, aNewItem);
            }
        }
    }


    private <T> void removeItem(List<T> currentItems, int position, AdapterNotifier adapterNotifier)
    {
        currentItems.remove(position);
        adapterNotifier.notifyItemRemoved(position);
    }


    private <T> void addItem(List<T> currentItems, int position, T item, AdapterNotifier adapterNotifier)
    {
        currentItems.add(position, item);
        adapterNotifier.notifyItemInserted(position);
    }


    private <T> void moveItem(List<T> currentItems, int fromPosition, int toPosition, AdapterNotifier adapterNotifier)
    {
        T itemToMove = currentItems.remove(fromPosition);
        currentItems.add(toPosition, itemToMove);
        adapterNotifier.notifyItemMoved(fromPosition, toPosition);
    }
}
