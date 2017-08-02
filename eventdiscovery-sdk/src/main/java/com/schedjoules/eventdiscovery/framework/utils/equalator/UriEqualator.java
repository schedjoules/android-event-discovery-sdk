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

package com.schedjoules.eventdiscovery.framework.utils.equalator;

import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.uris.Text;


/**
 * {@link Equalator} for {@link Uri} that compares the String representation of the full URI.
 *
 * @author Gabor Keszthelyi
 */
public final class UriEqualator implements Equalator<Uri>
{

    public static Equalator<Uri> INSTANCE = new UriEqualator();


    private UriEqualator()
    {

    }


    @Override
    public boolean areEqual(Uri left, Uri right)
    {
        String leftStr = new Text(left).toString();
        String rightStr = new Text(right).toString();
        return leftStr.equals(rightStr);
    }
}
