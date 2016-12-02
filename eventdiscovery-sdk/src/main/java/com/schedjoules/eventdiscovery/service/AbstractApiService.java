package com.schedjoules.eventdiscovery.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.schedjoules.client.Api;
import com.schedjoules.client.ApiQuery;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * @author Marten Gajda
 */
public abstract class AbstractApiService extends Service
{
    /**
     * A {@link Binder} that gives access to the SchedJoules API.
     */
    private final static class ApiServiceBinder extends Binder implements ApiService
    {

        private final Api mApi;


        public ApiServiceBinder(@NonNull Api api)
        {
            mApi = api;
        }


        @NonNull
        @Override
        public <T> T apiResponse(@NonNull ApiQuery<T> query) throws URISyntaxException, ProtocolError, ProtocolException, IOException
        {
            return query.queryResult(mApi);
        }
    }


    /**
     * A factory that creates Api instances.
     */
    public interface ApiFactory
    {
        /**
         * Create a new SchedJoules {@link Api} instance.
         *
         * @param context
         *         A Context.
         *
         * @return
         */
        @NonNull
        Api schedJoulesApi(@NonNull Context context);
    }


    private final ApiFactory mApiFactory;


    public AbstractApiService(@NonNull ApiFactory apiFactory)
    {
        mApiFactory = apiFactory;
    }


    @Nullable
    @Override
    public final IBinder onBind(Intent intent)
    {
        return new ApiServiceBinder(mApiFactory.schedJoulesApi(this));
    }
}
