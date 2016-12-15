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

import android.support.annotation.NonNull;

import org.dmfs.android.dumbledore.WizardStep;
import org.dmfs.android.dumbledore.operations.ForwardXFadeOperation;
import org.dmfs.android.dumbledore.transitions.AbstractWizardTransition;
import org.dmfs.android.dumbledore.transitions.WizardTransition;


/**
 * A {@link WizardTransition} that moves on to the next {@link WizardStep} using a {@link ForwardXFadeOperation}.
 *
 * @author Marten Gajda
 */
public final class AutomaticWizardTransition extends AbstractWizardTransition
{
    public AutomaticWizardTransition(@NonNull WizardStep nextStep)
    {
        super(new ForwardFadeOperation(nextStep));
    }
}
