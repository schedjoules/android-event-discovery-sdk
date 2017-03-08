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
import com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermission;
import com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus;
import com.schedjoules.eventdiscovery.framework.searchlist.NoOpSearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModule;
import com.schedjoules.eventdiscovery.framework.searchlist.SearchModuleFactory;
import com.schedjoules.eventdiscovery.framework.searchlist.predicate.QueryPredicate;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdateListener;

import static com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus.DENIED_WITH_NEVER_ASK_AGAIN;
import static com.schedjoules.eventdiscovery.framework.permission.fiveway.FiveWayPermissionStatus.NOT_IN_MANIFEST;


/**
 * {@link SearchModuleFactory} for {@link PermissionProxy}.
 * <p>
 * It creates a {@link NoOpSearchModule} if the permission has status {@link FiveWayPermissionStatus#DENIED_WITH_NEVER_ASK_AGAIN} or {@link
 * FiveWayPermissionStatus#NOT_IN_MANIFEST}. Otherwise it decorates the given delegate module with a {@link PermissionProxy}.
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
        FiveWayPermission permission = new FiveWayPermission(activity, mPermissionName);
        FiveWayPermissionStatus status = permission.status();

        if (status == DENIED_WITH_NEVER_ASK_AGAIN || status == NOT_IN_MANIFEST)
        {
            return new NoOpSearchModule();
        }

        SearchModule delegateModule = mDelegateModuleFactory.create(activity, updateListener, itemChosenAction);

        return new PermissionProxy(
                delegateModule,
                mQueryPredicate,
                updateListener,
                permission,
                activity.getString(mNotAskedYetMessage),
                activity.getString(mDeniedMessage),
                activity.getString(mDeniedWithNeverAskAgainMessage));
    }
}
