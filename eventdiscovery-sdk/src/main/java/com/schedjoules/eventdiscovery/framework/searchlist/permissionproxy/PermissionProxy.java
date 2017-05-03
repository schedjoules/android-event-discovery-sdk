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

import android.app.Activity;

import com.schedjoules.eventdiscovery.R;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.list.smart.Clickable;
import com.schedjoules.eventdiscovery.framework.locationpicker.listitems.ActionMessageItem;
import com.schedjoules.eventdiscovery.framework.locationpicker.listitems.ButtonItem;
import com.schedjoules.eventdiscovery.framework.locationpicker.listitems.MessageItem;
import com.schedjoules.eventdiscovery.framework.permissions.Permission;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.predicate.QueryPredicate;
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
    private final Activity mActivity;
    private final SearchModule mDelegate;
    private final QueryPredicate mQueryPredicate;
    private final ResultUpdateListener<ListItem> mUpdateListener;
    private final Permission mPermission;
    private final String mNotAskedYetMessage;
    private final String mDeniedMessage;
    private final String mDeniedWithNeverAskAgainMessage;
    private boolean mPermissionRequested;


    /**
     * Constructor.
     *
     * @param activity
     *         The current {@link Activity}.
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
    public PermissionProxy(Activity activity,
                           SearchModule delegate,
                           QueryPredicate queryPredicate,
                           ResultUpdateListener<ListItem> updateListener,
                           Permission permission,
                           String notAskedYetMessage,
                           String deniedMessage,
                           String deniedWithNeverAskAgainMessage)
    {
        mActivity = activity;
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
            if (mPermission.isGranted())
            {
                mDelegate.onSearchQueryChange(newQuery);
            }
            else
            {
                if (mPermissionRequested)
                {
                    Activity activity = mActivity;
                    if (activity != null && mPermission.isRequestable(activity))
                    {
                        mUpdateListener.onUpdate(
                                new ShowSingle<ListItem>(
                                        new ActionMessageItem(mDeniedMessage, mActivity.getString(R.string.schedjoules_button_grant_permission),
                                                new PermissionRequestOnClick()),
                                        newQuery));
                    }
                    else
                    {
                        mUpdateListener.onUpdate(new ShowSingle<ListItem>(new MessageItem(mDeniedWithNeverAskAgainMessage), newQuery));
                    }
                }
                else
                {
                    mUpdateListener.onUpdate(
                            new ShowSingle<ListItem>(
                                    new Clickable<>(
                                            new ButtonItem(mNotAskedYetMessage),
                                            new PermissionRequestOnClick(), "permissionRequest"),
                                    newQuery));
                }
            }
        }
    }


    private final class PermissionRequestOnClick implements OnClickAction
    {
        @Override
        public void onClick()
        {
            mPermissionRequested = true;
            mPermission.request().send(mActivity);
        }

    }
}
