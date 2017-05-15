package com.schedjoules.eventdiscovery.demo;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

import com.schedjoules.client.Api;
import com.schedjoules.client.SchedJoulesApi;
import com.schedjoules.client.utils.StringAccessToken;
import com.schedjoules.eventdiscovery.demo.utils.ResponseLogging;
import com.schedjoules.eventdiscovery.framework.http.DefaultExecutor;
import com.schedjoules.eventdiscovery.framework.http.RequestUriAndTimeLogging;
import com.schedjoules.eventdiscovery.service.AbstractApiService;


/**
 * A {@link Service} that connects to the {@link SchedJoulesApi}.
 *
 * @author Marten Gajda
 */
public final class ApiService extends AbstractApiService
{
    public ApiService()
    {
        super(new ApiFactory()
        {
            @NonNull
            @Override
            public Api schedJoulesApi(@NonNull Context context)
            {
                return new DefaultApi(
                        context,
                        new StringAccessToken("0443a55244bb2b6224fd48e0416f0d9c"),
                        new RequestUriAndTimeLogging(
                                new ResponseLogging(new DefaultExecutor(context), true, "EventDiscovery-Request"),
                                BuildConfig.LOG_REQUESTS, "EventDiscovery-Request"));
            }
        });
    }
}
