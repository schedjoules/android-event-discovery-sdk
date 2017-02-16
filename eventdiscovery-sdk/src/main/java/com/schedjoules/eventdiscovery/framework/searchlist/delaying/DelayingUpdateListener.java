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

package com.schedjoules.eventdiscovery.framework.searchlist.delaying;

import android.os.Handler;
import android.os.Looper;

import com.schedjoules.eventdiscovery.framework.list.ListItem;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.ResultUpdate;
import com.schedjoules.eventdiscovery.framework.searchlist.resultupdates.SectionedResultUpdateListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Decorator for {@link SectionedResultUpdateListener} that delays passing on the {@link ResultUpdate} when needed to not 'overlap' animations.
 *
 * @author Gabor Keszthelyi
 */
public final class DelayingUpdateListener implements SectionedResultUpdateListener<ListItem>
{
    private final SectionedResultUpdateListener<ListItem> mDelegate;
    private final long mWaitMillis;
    private final long mDelayMillis;

    private final Queue<Runnable> mQueue;
    private final Handler mMainHandler;

    private long mLastUpdateFiredTime;
    private boolean mIsScheduled;


    /**
     * @param waitMillis
     *         the time while a new incoming update will be waited for others to arrive before firing them together
     * @param delayMillis
     *         the minimum time between fired updates, to let the previous animation finish
     */
    public DelayingUpdateListener(SectionedResultUpdateListener<ListItem> delegate, long waitMillis, long delayMillis)
    {
        mDelegate = delegate;
        mWaitMillis = waitMillis;
        mDelayMillis = delayMillis;

        mQueue = new LinkedList<>();
        mMainHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void onUpdate(int sectionNumber, ResultUpdate<ListItem> sectionUpdate)
    {
        mQueue.add(new SingleUpdate(sectionNumber, sectionUpdate));

        if (mIsScheduled)
        {
            // An Runnable to run all pending updates from the queue is already scheduled, no need to do anything else.
            return;
        }

        long timeSinceLastFire = System.currentTimeMillis() - mLastUpdateFiredTime;
        long delay = timeSinceLastFire < mDelayMillis ? mDelayMillis - timeSinceLastFire : mWaitMillis;

        mMainHandler.postDelayed(new RunAllUpdates(), delay);

        mIsScheduled = true;
    }


    private class SingleUpdate implements Runnable
    {
        private final int mSectionNumber;
        private final ResultUpdate<ListItem> mSectionUpdate;


        private SingleUpdate(int sectionNumber, ResultUpdate<ListItem> sectionUpdate)
        {
            mSectionNumber = sectionNumber;
            mSectionUpdate = sectionUpdate;
        }


        @Override
        public void run()
        {
            mDelegate.onUpdate(mSectionNumber, mSectionUpdate);
        }
    }


    private class RunAllUpdates implements Runnable
    {

        @Override
        public void run()
        {
            mLastUpdateFiredTime = System.currentTimeMillis();

            Iterator<Runnable> iterator = mQueue.iterator();
            while (iterator.hasNext())
            {
                iterator.next().run();
                iterator.remove();
            }
            mIsScheduled = false;
        }
    }
}
