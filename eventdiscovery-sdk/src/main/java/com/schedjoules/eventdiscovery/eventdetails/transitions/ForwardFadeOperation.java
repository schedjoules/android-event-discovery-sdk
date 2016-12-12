/*
 * Copyright 2016 SchedJoules
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

package com.schedjoules.eventdiscovery.eventdetails.transitions;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.schedjoules.eventdiscovery.R;

import org.dmfs.android.dumbledore.WizardStep;
import org.dmfs.android.dumbledore.operations.AbstractWizardOperation;
import org.dmfs.android.dumbledore.operations.WizardOperation;


/**
 * A {@link WizardOperation} that moves on to the next {@link WizardStep} using a cross-fade animation.
 *
 * @author Marten Gajda
 */
public final class ForwardFadeOperation extends AbstractWizardOperation
{
    private final WizardStep mNextStep;


    /**
     * Creates a new {@link WizardOperation} that x-fades to the next {@link WizardStep}.
     *
     * @param nextStep
     *         The next {@link WizardStep}.
     */
    public ForwardFadeOperation(@NonNull WizardStep nextStep)
    {
        this.mNextStep = nextStep;
    }


    @Override
    public void apply(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull WizardStep previousStep)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (useRtl(context))
        {
            transaction.setCustomAnimations(
                    R.anim.schedjoules_fade_in, R.anim.schedjoules_anim_none,
                    org.dmfs.android.dumbledore.R.anim.wand_wizard_enter_left,
                    org.dmfs.android.dumbledore.R.anim.wand_wizard_exit_right);
        }
        else
        {
            // apparently the new fragment is underneath to old one, so fade th old one out instead of fading the new one in
            transaction.setCustomAnimations(
                    R.anim.schedjoules_anim_none, R.anim.schedjoules_fade_out,
                    org.dmfs.android.dumbledore.R.anim.wand_wizard_enter_right,
                    org.dmfs.android.dumbledore.R.anim.wand_wizard_exit_left);
        }
        transaction.replace(org.dmfs.android.dumbledore.R.id.dumbledore_wizard_host, mNextStep.fragment(context));
        if (fragmentManager.getBackStackEntryCount() > 0 || !previousStep.skipOnBack())
        {
            transaction.addToBackStack(mNextStep.skipOnBack() ? "skip" : null);
        }
        transaction.commit();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(mNextStep, flags);
    }


    public final static Creator<org.dmfs.android.dumbledore.operations.ForwardXFadeOperation> CREATOR = new Creator<org.dmfs.android.dumbledore.operations.ForwardXFadeOperation>()
    {
        @Override
        public org.dmfs.android.dumbledore.operations.ForwardXFadeOperation createFromParcel(Parcel source)
        {
            return new org.dmfs.android.dumbledore.operations.ForwardXFadeOperation((WizardStep) source.readParcelable(getClass().getClassLoader()));
        }


        @Override
        public org.dmfs.android.dumbledore.operations.ForwardXFadeOperation[] newArray(int size)
        {
            return new org.dmfs.android.dumbledore.operations.ForwardXFadeOperation[size];
        }
    };
}
