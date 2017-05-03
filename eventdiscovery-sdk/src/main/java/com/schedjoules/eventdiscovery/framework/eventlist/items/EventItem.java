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

package com.schedjoules.eventdiscovery.framework.eventlist.items;

import android.support.annotation.NonNull;
import android.view.View;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.insights.steps.Interaction;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.eventdetails.BasicEventDetails;
import com.schedjoules.eventdiscovery.framework.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.datetime.FormattedDateTime;
import com.schedjoules.eventdiscovery.framework.datetime.ShortTime;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.model.Equalable;
import com.schedjoules.eventdiscovery.framework.utils.InsightsTask;
import com.schedjoules.eventdiscovery.framework.utils.SchedJoulesLinks;

import org.dmfs.httpessentials.types.StringToken;


/**
 * List item in the even list UI for a certain {@link Event}
 *
 * @author Gabor Keszthelyi
 */
public final class EventItem implements ListItem<EventItemView>
{
    private final Event mEvent;
    private final FormattedDateTime mFormattedTime;


    public EventItem(Event event)
    {
        mEvent = event;
        mFormattedTime = new ShortTime(event.start());
    }


    @Override
    public int layoutResId()
    {
        return R.layout.schedjoules_list_item_event;
    }


    @Override
    public void bindDataTo(EventItemView view)
    {
        view.setTitle(mEvent.title());
        view.setTime(mFormattedTime.value(view.getContext()));
        if (mEvent.locations().iterator().hasNext())
        {
            view.setLocation(mEvent.locations().iterator().next().name());
        }

        // TODO Set category when available

        view.setThumbnail(new SchedJoulesLinks(mEvent.links()).thumbnailUri());

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onItemClick(v);

            }
        });

    }


    @NonNull
    @Override
    public Equalable id()
    {
        throw new UnsupportedOperationException("ListItem id is not supported in Event List currently");
    }


    private void onItemClick(View v)
    {
        BaseActivity activity = (BaseActivity) v.getContext();

        new InsightsTask(activity).execute(new Interaction(new StringToken("open-event"), mEvent));

        boolean twoPane = activity.getResources()
                .getBoolean(R.bool.schedjoules_hasTwoPanes);

        if (twoPane)
        {
            // currently not supported
//            new Fragments(activity).replace(
//                    R.id.schedjoules_event_detail_container,
//                    EventDetailsFragment.newInstance(mEvent));
        }
        else
        {
            new BasicEventDetails(mEvent).show(activity);
        }
    }


    @Override
    public String toString()
    {
        return mEvent.title();
    }

}
