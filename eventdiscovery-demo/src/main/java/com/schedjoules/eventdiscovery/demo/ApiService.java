package com.schedjoules.eventdiscovery.demo;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

import com.schedjoules.client.Api;
import com.schedjoules.client.SchedJoulesApi;
import com.schedjoules.client.SchedJoulesApiClient;
import com.schedjoules.client.utils.StringAccessToken;
import com.schedjoules.eventdiscovery.framework.http.RequestUriLogging;
import com.schedjoules.eventdiscovery.framework.utils.SharedPrefsUserIdentifier;
import com.schedjoules.eventdiscovery.service.AbstractApiService;

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.executors.retrying.Retrying;
import org.dmfs.httpessentials.executors.retrying.policies.DefaultRetryPolicy;
import org.dmfs.httpessentials.executors.useragent.Branded;
import org.dmfs.httpessentials.httpurlconnection.HttpUrlConnectionExecutor;
import org.dmfs.httpessentials.httpurlconnection.factories.DefaultHttpUrlConnectionFactory;
import org.dmfs.httpessentials.httpurlconnection.factories.decorators.Finite;
import org.dmfs.httpessentials.types.VersionedProduct;


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
                SchedJoulesApiClient client = new SchedJoulesApiClient(new StringAccessToken("0443a55244bb2b6224fd48e0416f0d9c"));

                HttpRequestExecutor executor =
                        new RequestUriLogging(
                                new Branded(
                                        new Retrying(
                                                new HttpUrlConnectionExecutor(
                                                        new Finite(
                                                                new DefaultHttpUrlConnectionFactory(),
                                                                5000, 5000)),
                                                new DefaultRetryPolicy(3)),
                                        // make sure to use the BuildConfig of your application
                                        new VersionedProduct(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME)),
                                BuildConfig.LOG_REQUESTS, "EventDiscovery-Request");

                return new SchedJoulesApi(client, executor, new SharedPrefsUserIdentifier(context));
            }
        });
    }
}
