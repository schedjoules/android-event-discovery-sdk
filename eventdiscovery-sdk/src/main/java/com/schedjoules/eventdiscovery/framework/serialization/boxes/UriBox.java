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

package com.schedjoules.eventdiscovery.framework.serialization.boxes;

import android.os.Parcel;

import com.schedjoules.eventdiscovery.framework.serialization.core.Box;

import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc3986.uris.Text;


/**
 * {@link Box} for {@link Uri}.
 *
 * @author Gabor Keszthelyi
 */
public final class UriBox implements Box<Uri>
{
    private final Uri mUri;


    public UriBox(Uri uri)
    {
        mUri = uri;
    }


    @Override
    public Uri content()
    {
        return mUri;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(new Text(mUri).toString());
    }


    public static final Creator<UriBox> CREATOR = new Creator<UriBox>()
    {
        @Override
        public UriBox createFromParcel(Parcel in)
        {
            return new UriBox(new LazyUri(new Precoded(in.readString())));
        }


        @Override
        public UriBox[] newArray(int size)
        {
            return new UriBox[size];
        }
    };
}
