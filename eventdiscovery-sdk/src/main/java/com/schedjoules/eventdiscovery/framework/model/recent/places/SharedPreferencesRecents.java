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

package com.schedjoules.eventdiscovery.framework.model.recent.places;

import android.content.Context;
import android.content.SharedPreferences;

import com.schedjoules.eventdiscovery.framework.model.recent.CharSequenceConverter;
import com.schedjoules.eventdiscovery.framework.model.recent.Recent;
import com.schedjoules.eventdiscovery.framework.model.recent.Recents;

import org.dmfs.iterators.Function;
import org.dmfs.iterators.decorators.Mapped;
import org.dmfs.rfc3986.encoding.Encoded;

import java.util.Iterator;
import java.util.Map;


/**
 * {@link Recents} of values serialized to {@link CharSequence} and stored in {@link SharedPreferences}.
 *
 * @author Marten Gajda
 */
public final class SharedPreferencesRecents<T> implements Recents<T>
{
    private final SharedPreferences mSharedPreferences;
    private final CharSequenceConverter<T> mFactory;


    public SharedPreferencesRecents(Context context, String name, CharSequenceConverter<T> factory)
    {
        mSharedPreferences = context.getSharedPreferences("com.schedjoules.recent_" + new Encoded(name), 0);
        mFactory = factory;
    }


    @Override
    public Recent<T> recent(T value)
    {
        return new SharedPreferencesRecent<>(mSharedPreferences, mFactory, mFactory.fromValue(value).toString());
    }


    @Override
    public Iterator<Recent<T>> iterator()
    {
        final Map<String, ?> all = mSharedPreferences.getAll();
        return new Mapped<>(all.keySet().iterator(), new Function<String, Recent<T>>()
        {
            @Override
            public Recent<T> apply(String element)
            {
                return new SharedPreferencesRecent<>(mSharedPreferences, mFactory, element);
            }
        });
    }


    private final static class SharedPreferencesRecent<T> implements Recent<T>
    {
        private final SharedPreferences mSharedPreferences;
        private final CharSequenceConverter<T> mFactory;
        private final String mKey;


        public SharedPreferencesRecent(SharedPreferences sharedPreferences, CharSequenceConverter<T> factory, String key)
        {
            mSharedPreferences = sharedPreferences;
            mFactory = factory;
            mKey = key;
        }


        @Override
        public long timestamp()
        {
            return mSharedPreferences.getLong(mKey, 0);
        }


        @Override
        public T value()
        {
            return mFactory.fromCharSequence(mKey);
        }


        @Override
        public void remember()
        {
            mSharedPreferences.edit().putLong(mKey, System.currentTimeMillis()).apply();
        }


        @Override
        public void forget()
        {
            mSharedPreferences.edit().remove(mKey).apply();
        }
    }
}
