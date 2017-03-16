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
import android.support.annotation.StringRes;

import com.schedjoules.eventdiscovery.framework.list.ItemChosenAction;
import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.permissions.AppPermissions;
import com.schedjoules.eventdiscovery.framework.permissions.BasicAppPermissions;
import com.schedjoules.eventdiscovery.framework.permissions.Permission;
import com.schedjoules.eventdiscovery.framework.searchlist.NoOpSearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.predicate.QueryPredicate;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;


/**
 * {@link SearchModuleFactory} for {@link PermissionProxy}.
 *
 * @author Gabor Keszthelyi
 */
public abstract class AbstractPermissionProxyModuleFactory<ITEM_DATA> implements SearchModuleFactory<ITEM_DATA>
{
    private final SearchModuleFactory<ITEM_DATA> mDelegateModuleFactory;
    private final QueryPredicate mQueryPredicate;
    private final String mPermissionName;
    private final int mNotAskedYetMessage;
    private final int mDeniedMessage;
    private final int mDeniedWithNeverAskAgainMessage;


    public AbstractPermissionProxyModuleFactory(SearchModuleFactory<ITEM_DATA> delegateModuleFactory,
                                                QueryPredicate queryPredicate,
                                                String permissionName,
                                                @StringRes int notAskedYetMessage,
                                                @StringRes int deniedMessage,
                                                @StringRes int deniedWithNeverAskAgainMessage)
    {
        mDelegateModuleFactory = delegateModuleFactory;
        mQueryPredicate = queryPredicate;
        mPermissionName = permissionName;
        mNotAskedYetMessage = notAskedYetMessage;
        mDeniedMessage = deniedMessage;
        mDeniedWithNeverAskAgainMessage = deniedWithNeverAskAgainMessage;
    }


    @Override
    public final SearchModule create(Activity activity, ResultUpdateListener<ListItem> updateListener, ItemChosenAction<ITEM_DATA> itemChosenAction)
    {
        AppPermissions appPermissions = new BasicAppPermissions(activity);
        Permission permission = appPermissions.forName(mPermissionName);

        if (!permission.isRequestable(activity))
        {
            return new NoOpSearchModule();
        }

        SearchModule delegateModule = mDelegateModuleFactory.create(activity, updateListener, itemChosenAction);

        return new PermissionProxy(
                activity,
                delegateModule,
                mQueryPredicate,
                updateListener,
                permission,
                activity.getString(mNotAskedYetMessage),
                activity.getString(mDeniedMessage),
                activity.getString(mDeniedWithNeverAskAgainMessage));
    }
}
