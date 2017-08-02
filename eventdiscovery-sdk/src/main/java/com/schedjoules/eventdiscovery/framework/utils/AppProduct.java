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

package com.schedjoules.eventdiscovery.framework.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import com.schedjoules.eventdiscovery.framework.utils.factory.Factory;
import com.schedjoules.eventdiscovery.framework.utils.factory.Lazy;
import com.schedjoules.eventdiscovery.framework.utils.factory.SimpleLazy;

import org.dmfs.httpessentials.types.Product;
import org.dmfs.httpessentials.types.VersionedProduct;


/**
 * The {@link Product} of the current app.
 *
 * @author Marten Gajda
 */
public final class AppProduct implements Product
{
    private final Lazy<Product> mProduct;


    public AppProduct(final Context context)
    {
        mProduct = new SimpleLazy<>(new Factory<Product>()
        {
            @Override
            public Product create()
            {
                String packageName = context.getPackageName();
                try
                {
                    return new VersionedProduct(packageName, context.getPackageManager().getPackageInfo(packageName, 0).versionName);
                }
                catch (PackageManager.NameNotFoundException e)
                {
                    throw new RuntimeException("Own Packagename not found?! o_O");
                }
            }
        });
    }


    @Override
    public void appendTo(StringBuilder sb)
    {
        mProduct.get().appendTo(sb);
    }
}
