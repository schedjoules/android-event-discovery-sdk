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

package com.schedjoules.eventdiscovery.framework.model;

import com.schedjoules.client.eventsdiscovery.Envelope;

import org.dmfs.optional.Optional;


/**
 * {@link Envelope} which takes the ready properties as constructor parameters.
 *
 * @author Gabor Keszthelyi
 */
public final class StructuredEnvelope<T> implements Envelope<T>
{
    private final String mEtag;
    private final String mUid;
    private final Optional<T> mOptPayload;


    public StructuredEnvelope(String etag, String uid, Optional<T> optPayload)
    {
        mEtag = etag;
        mUid = uid;
        mOptPayload = optPayload;
    }


    @Override
    public String etag()
    {
        return mEtag;
    }


    @Override
    public String uid()
    {
        return mUid;
    }


    @Override
    public Optional<T> payload()
    {
        return mOptPayload;
    }
}
