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

package com.schedjoules.eventdiscovery.framework.googleapis.errors;

import com.google.android.gms.common.ConnectionResult;

import static com.google.android.gms.common.ConnectionResult.API_UNAVAILABLE;
import static com.google.android.gms.common.ConnectionResult.CANCELED;
import static com.google.android.gms.common.ConnectionResult.DEVELOPER_ERROR;
import static com.google.android.gms.common.ConnectionResult.INTERNAL_ERROR;
import static com.google.android.gms.common.ConnectionResult.INTERRUPTED;
import static com.google.android.gms.common.ConnectionResult.INVALID_ACCOUNT;
import static com.google.android.gms.common.ConnectionResult.LICENSE_CHECK_FAILED;
import static com.google.android.gms.common.ConnectionResult.NETWORK_ERROR;
import static com.google.android.gms.common.ConnectionResult.RESOLUTION_REQUIRED;
import static com.google.android.gms.common.ConnectionResult.RESTRICTED_PROFILE;
import static com.google.android.gms.common.ConnectionResult.SERVICE_DISABLED;
import static com.google.android.gms.common.ConnectionResult.SERVICE_INVALID;
import static com.google.android.gms.common.ConnectionResult.SERVICE_MISSING;
import static com.google.android.gms.common.ConnectionResult.SERVICE_MISSING_PERMISSION;
import static com.google.android.gms.common.ConnectionResult.SERVICE_UPDATING;
import static com.google.android.gms.common.ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED;
import static com.google.android.gms.common.ConnectionResult.SIGN_IN_FAILED;
import static com.google.android.gms.common.ConnectionResult.SIGN_IN_REQUIRED;
import static com.google.android.gms.common.ConnectionResult.TIMEOUT;


/**
 * Basic {@link GoogleApiErrorInterpreter}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicGoogleApiErrorInterpreter implements GoogleApiErrorInterpreter
{
    @Override
    public boolean isRetriable(ConnectionResult connectionResult)
    {
        // Meaning of the codes:
        // https://developers.google.com/android/reference/com/google/android/gms/common/ConnectionResult

        switch (connectionResult.getErrorCode())
        {
            case API_UNAVAILABLE:
            case LICENSE_CHECK_FAILED:
            case SERVICE_INVALID:
            case RESTRICTED_PROFILE:
            case SIGN_IN_FAILED:
            case INVALID_ACCOUNT:
                return false;

            case CANCELED:
            case INTERNAL_ERROR:
            case NETWORK_ERROR:
            case SERVICE_DISABLED: // auto-managed
            case SERVICE_MISSING: // auto-managed
            case SERVICE_MISSING_PERMISSION: // auto-managed
            case SERVICE_UPDATING:
            case SERVICE_VERSION_UPDATE_REQUIRED: // auto-managed
            case TIMEOUT:
            case RESOLUTION_REQUIRED: // auto-managed
            case SIGN_IN_REQUIRED: // auto-managed
            case INTERRUPTED:
                return true;

            case DEVELOPER_ERROR:
                throw new RuntimeException("Incorrect GoogleApiClient configuration, should not happen");

                // Not applicable: SUCCESS

            default:
                return true;
        }
    }

}
