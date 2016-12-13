package com.schedjoules.eventdiscovery.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.schedjoules.client.ApiQuery;
import com.schedjoules.eventdiscovery.utils.FutureLocalServiceConnection;
import com.schedjoules.eventdiscovery.utils.FutureServiceConnection;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;


/**
 * The interface of the services that provides a proxy to the server API.
 *
 * @author Marten Gajda
 */
public interface ApiService
{
    /**
     * A {@link FutureServiceConnection} to the {@link ApiService}
     */
    final class FutureConnection implements FutureServiceConnection<ApiService>
    {
        private final FutureLocalServiceConnection<ApiService> mDelegate;


        /**
         * Creates a future connection to the {@link ApiService} using the given context.
         *
         * @param context
         */
        public FutureConnection(Context context)
        {
            mDelegate = new FutureLocalServiceConnection<>(context, new Intent("com.schedjoules.API").setPackage(context.getPackageName()));
        }


        @Override
        public boolean isConnected()
        {
            return mDelegate.isConnected();
        }


        @Override
        public ApiService service(long timeout) throws TimeoutException, InterruptedException
        {
            return mDelegate.service(timeout);
        }


        @Override
        public void disconnect()
        {
            mDelegate.disconnect();
        }
    }

    @NonNull
    @WorkerThread
    <T> T apiResponse(@NonNull ApiQuery<T> query) throws URISyntaxException, ProtocolError, ProtocolException, IOException;
}
