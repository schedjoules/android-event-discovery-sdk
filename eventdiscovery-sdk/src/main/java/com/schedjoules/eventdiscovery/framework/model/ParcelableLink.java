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

import android.os.Parcel;
import android.os.Parcelable;

import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.parameters.BasicParameterType;
import org.dmfs.httpessentials.parameters.Parameter;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.typedentity.EntityConverter;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.iterators.EmptyIterator;
import org.dmfs.iterators.SingletonIterator;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * A {@link Link} that can be parcelled.
 *
 * @author Marten Gajda
 */
public final class ParcelableLink implements Link, Parcelable
{
    private final static EntityConverter<String> STRING_CONVERTER = new PlainStringHeaderConverter();
    private final static ParameterType<String> PARAM_ACTION_TYPE = new BasicParameterType<>("http://schedjoules.com/booking/type", STRING_CONVERTER);
    private final static ParameterType<String> PARAM_ACTION_MIN_PRICE = new BasicParameterType<>("http://schedjoules.com/booking/min-price", STRING_CONVERTER);
    private final static ParameterType<String> PARAM_ACTION_MAX_PRICE = new BasicParameterType<>("http://schedjoules.com/booking/max-price", STRING_CONVERTER);
    private final static ParameterType<String> PARAM_IMAGE_WIDTH = new BasicParameterType<>("http://schedjoules.com/prop/width", STRING_CONVERTER);
    private final static ParameterType<String> PARAM_IMAGE_HEIGHT = new BasicParameterType<>("http://schedjoules.com/prop/height", STRING_CONVERTER);
    public static final Creator<ParcelableLink> CREATOR = new Creator<ParcelableLink>()
    {
        @Override
        public ParcelableLink createFromParcel(Parcel in)
        {
            String target = in.readString();
            String title = in.readString();
            List<String> relationTypes = new ArrayList<>();
            in.readStringList(relationTypes);
            List<String> reverseRelationTypes = new ArrayList<>();
            in.readStringList(reverseRelationTypes);

            Map<String, String> propertyMap = new HashMap<>(16);
            loadProperty(in, PARAM_ACTION_TYPE, propertyMap);
            loadProperty(in, PARAM_ACTION_MIN_PRICE, propertyMap);
            loadProperty(in, PARAM_ACTION_MAX_PRICE, propertyMap);
            loadProperty(in, PARAM_IMAGE_WIDTH, propertyMap);
            loadProperty(in, PARAM_IMAGE_HEIGHT, propertyMap);
            return new ParcelableLink(new UnparcelledLink(target, title, relationTypes, reverseRelationTypes, propertyMap));
        }


        private void loadProperty(Parcel in, ParameterType<String> parameterType, Map<String, String> propertyMap)
        {
            String value = in.readString();
            if (value == null)
            {
                // no value
                return;
            }
            propertyMap.put(parameterType.name(), value);
        }


        @Override
        public ParcelableLink[] newArray(int size)
        {
            return new ParcelableLink[size];
        }
    };
    private final Link mDelegate;


    public ParcelableLink(Link delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public URI target()
    {
        return mDelegate.target();
    }


    @Override
    public URI context(URI defaultContext)
    {
        return mDelegate.context(defaultContext);
    }


    @Override
    public Set<Locale> languages()
    {
        return mDelegate.languages();
    }


    @Override
    public String title()
    {
        return mDelegate.title();
    }


    @Override
    public MediaType mediaType()
    {
        return mDelegate.mediaType();
    }


    @Override
    public Set<String> relationTypes()
    {
        return mDelegate.relationTypes();
    }


    @Override
    public Set<String> reverseRelationTypes()
    {
        return mDelegate.reverseRelationTypes();
    }


    @Override
    public <T> Parameter<T> firstParameter(ParameterType<T> parameterType, T defaultValue)
    {
        return mDelegate.firstParameter(parameterType, defaultValue);
    }


    @Override
    public <T> Iterator<Parameter<T>> parameters(ParameterType<T> parameterType)
    {
        return mDelegate.parameters(parameterType);
    }


    @Override
    public <T> boolean hasParameter(ParameterType<T> parameterType)
    {
        return mDelegate.hasParameter(parameterType);
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        URI target = target();
        dest.writeString(target == null ? null : target().toString());
        dest.writeString(title());
        dest.writeStringList(new ArrayList<>(relationTypes()));
        dest.writeStringList(new ArrayList<>(reverseRelationTypes()));

        dest.writeString(firstParameter(PARAM_ACTION_TYPE, null).value());
        dest.writeString(firstParameter(PARAM_ACTION_MIN_PRICE, null).value());
        dest.writeString(firstParameter(PARAM_ACTION_MAX_PRICE, null).value());
        dest.writeString(firstParameter(PARAM_IMAGE_WIDTH, null).value());
        dest.writeString(firstParameter(PARAM_IMAGE_HEIGHT, null).value());
    }


    private final static class UnparcelledLink implements Link
    {
        private final String mTarget;
        private final String mTitle;
        private final List<String> mRelationTypes;
        private final List<String> mReverseRelationTypes;
        private final Map<String, String> mProperties;


        private UnparcelledLink(String target, String title, List<String> relationTypes, List<String> reverseRelationTypes, Map<String, String> properties)
        {
            mTarget = target;
            mTitle = title;
            mRelationTypes = relationTypes;
            mReverseRelationTypes = reverseRelationTypes;
            mProperties = properties;
        }


        @Override
        public URI target()
        {
            return mTarget == null ? null : URI.create(mTarget);
        }


        @Override
        public URI context(URI defaultContext)
        {
            throw new UnsupportedOperationException("Not implemented for unparcelled Link");
        }


        @Override
        public Set<Locale> languages()
        {
            throw new UnsupportedOperationException("Not implemented for unparcelled Link");
        }


        @Override
        public String title()
        {
            return mTitle;
        }


        @Override
        public MediaType mediaType()
        {
            throw new UnsupportedOperationException("Not implemented for unparcelled Link");
        }


        @Override
        public Set<String> relationTypes()
        {
            return new HashSet<>(mRelationTypes);
        }


        @Override
        public Set<String> reverseRelationTypes()
        {
            return new HashSet<>(mReverseRelationTypes);
        }


        @Override
        public <T> Parameter<T> firstParameter(ParameterType<T> parameterType, T defaultValue)
        {
            if (!mProperties.containsKey(parameterType.name()))
            {
                return parameterType.entity(defaultValue);
            }
            return parameterType.entityFromString(mProperties.get(parameterType.name()));
        }


        @Override
        public <T> Iterator<Parameter<T>> parameters(ParameterType<T> parameterType)
        {
            if (!mProperties.containsKey(parameterType.name()))
            {
                return EmptyIterator.instance();
            }
            return new SingletonIterator<>(parameterType.entityFromString(mProperties.get(parameterType.name())));
        }


        @Override
        public <T> boolean hasParameter(ParameterType<T> parameterType)
        {
            return mProperties.containsKey(parameterType.name());
        }
    }
}
