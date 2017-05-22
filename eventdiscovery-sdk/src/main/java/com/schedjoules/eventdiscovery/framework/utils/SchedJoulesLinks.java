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

import android.net.Uri;

import com.schedjoules.client.eventsdiscovery.Event;
import com.schedjoules.eventdiscovery.framework.model.ApiLink;

import org.dmfs.httpessentials.types.Link;

import java.net.URI;


/**
 * Represents schedjoules api related {@link Link}s among {@link Event#links()}.
 *
 * @author Gabor Keszthelyi
 */
public final class SchedJoulesLinks
{
    private final Iterable<Link> mLinks;


    public SchedJoulesLinks(Iterable<Link> links)
    {
        mLinks = links;
    }


    public Uri thumbnailUri()
    {
        return uriForRel(ApiLink.Rel.THUMBNAIL);
    }


    public Uri bannerUri()
    {
        return uriForRel(ApiLink.Rel.BANNER);
    }


    private Uri uriForRel(String rel)
    {
        for (Link link : mLinks)
        {
            if (link.relationTypes().contains(rel))
            {
                URI target = link.target();
                return target == null ? null : Uri.parse(target.toString());
            }
        }
        return null;
    }
}
