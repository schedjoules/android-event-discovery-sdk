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

package com.schedjoules.eventdiscovery.framework.list;

import android.support.v7.widget.RecyclerView;


/**
 * Interface for the {@link RecyclerView.Adapter} notify methods.
 *
 * @author Gabor Keszthelyi
 */
public interface AdapterNotifier
{
    void notifyDataSetChanged();

    void notifyItemChanged(int position);

    void notifyItemChanged(int position, Object payload);

    void notifyItemRangeChanged(int positionStart, int itemCount);

    void notifyItemInserted(int position);

    void notifyItemMoved(int fromPosition, int toPosition);

    void notifyItemRangeInserted(int positionStart, int itemCount);

    void notifyItemRemoved(int position);

    void notifyItemRangeRemoved(int positionStart, int itemCount);
}
