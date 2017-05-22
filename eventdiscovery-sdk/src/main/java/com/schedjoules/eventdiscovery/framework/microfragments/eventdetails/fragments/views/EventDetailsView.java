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

package com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.fragments.views;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.databinding.SchedjoulesFragmentEventDetailsBinding;
import com.schedjoules.eventdiscovery.framework.actions.Action;
import com.schedjoules.eventdiscovery.framework.actions.ActionClickListener;
import com.schedjoules.eventdiscovery.framework.actions.BaseActionFactory;
import com.schedjoules.eventdiscovery.framework.actions.OptionalAction;
import com.schedjoules.eventdiscovery.framework.actions.TicketButtonAction;
import com.schedjoules.eventdiscovery.framework.common.BaseFragment;
import com.schedjoules.eventdiscovery.framework.microfragments.eventdetails.ShowEventMicroFragment;
import com.schedjoules.eventdiscovery.framework.model.ApiLink;
import com.schedjoules.eventdiscovery.framework.utils.VenueName;
import com.schedjoules.eventdiscovery.framework.utils.smartview.SmartView;
import com.schedjoules.eventdiscovery.framework.widgets.NoOpOnClickListener;

import org.dmfs.httpessentials.types.Link;
import org.dmfs.optional.NullSafe;
import org.dmfs.optional.Optional;

import java.util.List;


/**
 * Represents the event details screen view.
 *
 * @author Gabor Keszthelyi
 */
public final class EventDetailsView implements SmartView<ShowEventMicroFragment.EventParams>
{
    private final SchedjoulesFragmentEventDetailsBinding mViews;
    private final BaseFragment mFragment;


    public EventDetailsView(BaseFragment fragment, SchedjoulesFragmentEventDetailsBinding views)
    {
        mFragment = fragment;
        mViews = views;
    }


    @Override
    public void update(ShowEventMicroFragment.EventParams eventParams)
    {
        List<Link> actionLinks = eventParams.actions();
        final Event event = eventParams.event();

        new VenueNameView(mViews.schedjoulesEventDetailsVenueName).update(new VenueName(event.locations()));

        new EventDescriptionView(mViews.schedjoulesEventDetailsDescription).update(new NullSafe<>(event.description()));

        BaseActionFactory actionFactory = new BaseActionFactory();

        new ActionView(mViews.schedjoulesEventDetailsActionShare)
                .update(new OptionalAction(ApiLink.Rel.Action.SHARE, actionLinks, actionFactory, event));

        new ActionView(mViews.schedjoulesEventDetailsActionAddToCalendar)
                .update(new OptionalAction(ApiLink.Rel.Action.ADD_TO_CALENDAR, actionLinks, actionFactory, event));

        Optional<Action> directionsAction = new OptionalAction(ApiLink.Rel.Action.DIRECTIONS, actionLinks, actionFactory, event);
        new ActionView(mViews.schedjoulesEventDetailsActionDirections).update(directionsAction);

        new TicketButtonView(mViews).update(new TicketButtonAction(actionLinks, event));

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mFragment.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.schedjoules_event_details_map_frame, mapFragment)
                .commit();

        mapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                GeoLocation geoLocation = event.locations().iterator().next().geoLocation();
                LatLng latLng = new LatLng(geoLocation.latitude(), geoLocation.longitude());
                googleMap.addMarker(new MarkerOptions().position(latLng));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });
        mViews.schedjoulesEventDetailsMapHolder.schedjoulesEventDetailsMapTransparentOverlay
                .setOnClickListener(directionsAction.isPresent() ?
                        new ActionClickListener(directionsAction.value().actionExecutable())
                        : new NoOpOnClickListener() // Needed, otherwise the map is still 'usable'
                );
    }
}
