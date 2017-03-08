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

package com.schedjoules.eventdiscovery.framework.searchlist.permissionproxy;

import android.support.annotation.Nullable;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.smart.Clickable;
import com.schedjoules.eventdiscovery.framework.location.listitems.MessageItem;
import com.schedjoules.eventdiscovery.framework.permission.PermissionRequestCallback;
import com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermission;
import com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus;
import com.schedjoules.eventdiscovery.framework.searchlist.QueryPredicate;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.Clear;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ShowSingle;
import com.schedjoules.eventdiscovery.framework.utils.smartview.OnClickAction;


/**
 * A {@link SearchModule} control proxy to use before another module that requires a permission to operate. It shows appropriate messages and prompts user to
 * grant the permission.
 *
 * @author Gabor Keszthelyi
 */
public final class PermissionProxy implements SearchModule
{
    private final SearchModule mDelegate;
    private final QueryPredicate mQueryPredicate;
    private final ResultUpdateListener<ListItem> mUpdateListener;
    private final FiveWayPermission mPermission;
    private final String mNotAskedYetMessage;
    private final String mDeniedMessage;
    private final String mDeniedWithNeverAskAgainMessage;


    /**
     * Constructor.
     *
     * @param delegate
     *         the delegate module to dispatch queries to if the permission is granted
     * @param queryPredicate
     *         tells which queries are relevant to the delegate module, so permission request message items are not shown when queries are not relevant
     * @param updateListener
     *         see {@link SearchModuleFactory} for more
     * @param permission
     *         the permission that is required for the delegate module to operate
     * @param notAskedYetMessage
     *         the message that should be displayed as an item when the permission hasn't been asked yet
     * @param deniedMessage
     *         the message that should be displayed as an item when the permission has been denied
     * @param deniedWithNeverAskAgainMessage
     *         the message that should be displayed as an item when the permission has been denied with "Never ask again" checked
     */
    public PermissionProxy(SearchModule delegate,
                           QueryPredicate queryPredicate,
                           ResultUpdateListener<ListItem> updateListener,
                           FiveWayPermission permission,
                           String notAskedYetMessage,
                           String deniedMessage,
                           String deniedWithNeverAskAgainMessage)
    {
        mDelegate = delegate;
        mQueryPredicate = queryPredicate;
        mUpdateListener = updateListener;
        mPermission = permission;
        mNotAskedYetMessage = notAskedYetMessage;
        mDeniedMessage = deniedMessage;
        mDeniedWithNeverAskAgainMessage = deniedWithNeverAskAgainMessage;
    }


    @Override
    public void shutDown()
    {
    }


    @Override
    public void onSearchQueryChange(String newQuery)
    {
        if (!mQueryPredicate.isValid(newQuery))
        {
            mUpdateListener.onUpdate(new Clear<ListItem>(newQuery));
        }
        else
        {
            switch (mPermission.status())
            {
                case GRANTED:
                    mDelegate.onSearchQueryChange(newQuery);
                    break;

                case NOT_ASKED_YET:
                    showMessageItem(mNotAskedYetMessage, new PermissionRequestOnClick(newQuery), newQuery);
                    break;

                case DENIED:
                    showMessageItem(mDeniedMessage, new PermissionRequestOnClick(newQuery), newQuery);
                    break;

                case DENIED_WITH_NEVER_ASK_AGAIN:
                    showMessageItem(mDeniedWithNeverAskAgainMessage, null, newQuery);
                    break;

                default: // Should not happen
                    mUpdateListener.onUpdate(new Clear<ListItem>(newQuery));
            }
        }
    }


    private void showMessageItem(String message, @Nullable OnClickAction onClickAction, String query)
    {
        ListItem messageItem = onClickAction == null ?
                new MessageItem(message) : new Clickable<>(new MessageItem(message), onClickAction);
        mUpdateListener.onUpdate(new ShowSingle<>(messageItem, query));
    }


    private class PermissionRequestOnClick implements OnClickAction
    {

        private final String mQuery;


        PermissionRequestOnClick(String query)
        {
            mQuery = query;
        }


        @Override
        public void onClick()
        {
            mPermission.request(new OurPermissionRequestCallback(mQuery));
        }

    }


    private class OurPermissionRequestCallback implements PermissionRequestCallback<FiveWayPermissionStatus>
    {

        private final String mQuery;


        OurPermissionRequestCallback(String query)
        {
            mQuery = query;
        }


        @Override
        public void onResult(FiveWayPermissionStatus newStatus)
        {
            // "Re-fire" the query to this proxy
            onSearchQueryChange(mQuery);
        }


        @Override
        public void onInterrupt()
        {
            // Nothing to do here, the same message items is still shown, user can retry with that
        }
    }
}
