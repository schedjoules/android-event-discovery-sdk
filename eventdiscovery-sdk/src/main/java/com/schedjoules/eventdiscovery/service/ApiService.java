package com.schedjoules.eventdiscovery.service;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.schedjoules.client.ApiQuery;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * @author Marten Gajda
 */
public interface ApiService
{
    @NonNull
    @WorkerThread
    <T> T apiResponse(@NonNull ApiQuery<T> query) throws URISyntaxException, ProtocolError, ProtocolException, IOException;
}
