/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.eventlist.items;

import android.view.View;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.insights.steps.Interaction;
import com.schedjoules.eventdiscovery.BasicEventDetails;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.common.BaseActivity;
import com.schedjoules.eventdiscovery.framework.adapter.ListItem;
import com.schedjoules.eventdiscovery.framework.adapter.flexibleadapter.AbstractFlexibleSectionable;
import com.schedjoules.eventdiscovery.model.SchedJoulesLinks;
import com.schedjoules.eventdiscovery.utils.DateTimeFormatter;
import com.schedjoules.eventdiscovery.utils.InsightsTask;

import org.dmfs.httpessentials.types.StringToken;


/**
 * List item in the even list UI for a certain {@link Event}
 *
 * @author Gabor Keszthelyi
 */
public final class EventItem extends AbstractFlexibleSectionable<EventItemView> implements ListItem<EventItemView>
{
    private final Event mEvent;


    public EventItem(Event event)
    {
        mEvent = event;
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
        view.setDateTimes(DateTimeFormatter.shortEventStartTime(view.getContext(), mEvent));
        if (mEvent.locations().iterator().hasNext())
        {
            // TODO What and how to display from location(s)?
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
//                    EventDetailFragment.newInstance(mEvent));
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
