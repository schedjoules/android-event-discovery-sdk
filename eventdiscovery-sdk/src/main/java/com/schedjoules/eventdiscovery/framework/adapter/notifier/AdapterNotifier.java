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

package com.schedjoules.eventdiscovery.framework.adapter.notifier;

import android.support.v7.widget.RecyclerView;

import java.util.List;


/**
 * Interface for notifying the used {@link RecyclerView.Adapter} about the changes in the data set. Abstraction created to be able to handle multiple types of
 * adapters that have different notifying mechanisms.
 *
 * @author Gabor Keszthelyi
 */
public interface AdapterNotifier<ITEM>
{
    void notifyInitialItemsAdded(List initialItems); // used after rotation for adapter that needs reference to the items

    void notifyNewItemsAdded(List newItems, int positionStart);

    void notifyNewItemAdded(ITEM item, int position);

    void notifyItemsCleared(int totalSize);

    void notifyItemRemoved(int position);
}
