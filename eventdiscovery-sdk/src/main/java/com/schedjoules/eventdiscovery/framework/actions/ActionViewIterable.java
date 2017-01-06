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

package com.schedjoules.eventdiscovery.framework.actions;

import android.support.annotation.NonNull;
import android.view.View;

import org.dmfs.iterators.AbstractConvertedIterator;
import org.dmfs.iterators.ConvertedIterator;

import java.util.Iterator;


/**
 * An {@link Iterable} that converts an {@link Iterable} of {@link Action}s to {@link View}s.
 *
 * @author Gabor Keszthelyi
 * @author Marten Gajda
 */
public final class ActionViewIterable implements Iterable<View>
{
    private final Iterable<Action> mActionLinks;
    private final ActionViewFactory mActionViewFactory;


    public ActionViewIterable(@NonNull Iterable<Action> actions, @NonNull ActionViewFactory actionViewFactory)
    {
        mActionViewFactory = actionViewFactory;
        mActionLinks = actions;
    }


    @NonNull
    @Override
    public Iterator<View> iterator()
    {
        return new ConvertedIterator<>(mActionLinks.iterator(), new AbstractConvertedIterator.Converter<View, Action>()
        {
            @Override
            public View convert(Action element)
            {
                return mActionViewFactory.actionView(element);
            }
        });
    }


    /**
     * An Interface of a factory to create {@link View}s for {@link Action}s.
     */
    public interface ActionViewFactory
    {
        /**
         * Create a {@link View} for the given {@link Action}.
         *
         * @param action
         *
         * @return A {@link View} that represents the given {@link Action}.
         */
        @NonNull
        View actionView(@NonNull Action action);
    }
}
