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

package com.schedjoules.eventdiscovery.framework.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.schedjoules.client.eventsdiscovery.GeoLocation;
import com.schedjoules.client.eventsdiscovery.queries.Coverage;
import com.schedjoules.client.eventsdiscovery.queries.FluentCoverage;
import com.schedjoules.eventdiscovery.framework.utils.LastKnownLocation;
import com.schedjoules.eventdiscovery.framework.utils.NetworkCountryCode;
import com.schedjoules.eventdiscovery.framework.utils.SimCountryCode;
import com.schedjoules.eventdiscovery.service.ApiService;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.optional.Optional;
import org.dmfs.pigeonpost.Cage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.concurrent.TimeoutException;


/**
 * An {@link IntentService} that checks the coverage of the current country and returns the result.
 *
 * @author Marten Gajda
 */
public final class CoverageService extends IntentService
{
    public static final String EXTRA_NESTED_DATA = "com.schedjoules.nesteddata";
    public static final String KEY_PIGEONCAGE = "pigeoncage";

    private static final String KEY_LAST_RESULT = "last_result";
    private static final String KEY_LAST_CHECK = "last_check";

    private final static long CACHE_DURATION = 2 * 3600 * 1000L; // 2 hours


    public CoverageService()
    {
        super("CoverageService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        // this is where we cache the result
        SharedPreferences prefs = getSharedPreferences("com.schedjoules.coverage", 0);

        if (System.currentTimeMillis() - prefs.getLong(KEY_LAST_CHECK, 0) < CACHE_DURATION)
        {
            // just checked, don't ask the API again
            sendResult(intent, prefs.getBoolean(KEY_LAST_RESULT, false));
            return;
        }

        ApiService.FutureConnection apiConnection = new ApiService.FutureConnection(this);
        try
        {
            ApiService apiService = apiConnection.service(1000);
            // build the coverage api query
            FluentCoverage coverageQuery = new Coverage(Locale.getDefault());

            Optional<Token> networkCountry = new NetworkCountryCode(this);
            if (networkCountry.isPresent())
            {
                coverageQuery = coverageQuery.withOperatorCountry(networkCountry.value());
            }
            Optional<Token> simCountry = new SimCountryCode(this);
            if (simCountry.isPresent())
            {
                coverageQuery = coverageQuery.withSimCountry(simCountry.value());
            }
            // check if we know our geo location
            Optional<GeoLocation> lastKnownLocation = new LastKnownLocation(this);
            if (lastKnownLocation.isPresent())
            {
                coverageQuery = coverageQuery.withGeoLocation(lastKnownLocation.value());
            }

            boolean countryCovered = apiService.apiResponse(coverageQuery);
            sendResult(intent, countryCovered);
            prefs.edit()
                    .putBoolean(KEY_LAST_RESULT, countryCovered)
                    .putLong(KEY_LAST_CHECK, System.currentTimeMillis())
                    .apply();
        }
        catch (TimeoutException | InterruptedException | ProtocolError | IOException | URISyntaxException | ProtocolException e)
        {
            // request failed, fall back to cached result
            sendResult(intent, prefs.getBoolean(KEY_LAST_RESULT, false));
        }
        finally
        {
            apiConnection.disconnect();
        }
    }


    private void sendResult(Intent intent, boolean result)
    {
        Cage<Boolean> cage = intent.getBundleExtra(EXTRA_NESTED_DATA).getParcelable(KEY_PIGEONCAGE);
        cage.pigeon(result).send(this);
    }
}
