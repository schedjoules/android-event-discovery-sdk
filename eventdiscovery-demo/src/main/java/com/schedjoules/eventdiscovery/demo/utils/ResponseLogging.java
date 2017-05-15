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

package com.schedjoules.eventdiscovery.demo.utils;

import android.util.Log;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.decoration.ResponseDecorated;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.httpessentials.types.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.zip.GZIPInputStream;


/**
 * @author Gabor Keszthelyi
 */
public final class ResponseLogging implements HttpRequestExecutor
{
    private final HttpRequestExecutor mDelegate;
    private final boolean mEnabled;
    private final String mTag;


    public ResponseLogging(HttpRequestExecutor delegate, boolean enabled, String tag)
    {
        mDelegate = delegate;
        mEnabled = enabled;
        mTag = tag;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        if (!mEnabled)
        {
            return mDelegate.execute(uri, request);
        }

        return mDelegate.execute(uri, new ResponseDecorated<>(request, new Decoration<HttpResponse>()
        {
            @Override
            public HttpResponse decorated(HttpResponse original)
            {
                try
                {
                    Log.d(mTag, "Status: " + original.status().statusCode());
                    Log.d(mTag, "ContentType: " + original.responseEntity().contentType());
                    if (original.headers().contains(HttpHeaders.CONTENT_ENCODING))
                    {
                        String content = gzipInputStreamToString(original.responseEntity().contentStream());
                        Log.d(mTag, "Body: " + pretty(content));
                        return new CachedBodyReponse(original, content, true);

                    }
                    else
                    {
                        String content = inputStreamToString(original.responseEntity().contentStream());
                        Log.d(mTag, "Body: " + pretty(content));
                        return new CachedBodyReponse(original, content, false);
                    }
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }));
    }


    private static String pretty(String jsonString)
    {
        try
        {
            return new JSONObject(jsonString).toString(4);
        }
        catch (JSONException e)
        {
            try
            {
                return new JSONArray(jsonString).toString(4);
            }
            catch (JSONException e1)
            {
                return jsonString;
            }
        }
    }


    private final class CachedBodyReponse implements HttpResponse
    {

        private final HttpResponse mOriginal;
        private final String mBody;
        private final boolean mIsGzipped;


        public CachedBodyReponse(HttpResponse original, String body, boolean isGzipped)
        {
            mOriginal = original;
            mBody = body;
            mIsGzipped = isGzipped;
        }


        @Override
        public HttpStatus status()
        {
            return mOriginal.status();
        }


        @Override
        public Headers headers()
        {
            return mOriginal.headers();
        }


        @Override
        public HttpResponseEntity responseEntity()
        {
            return new HttpResponseEntity()
            {
                @Override
                public MediaType contentType() throws IOException
                {
                    return mOriginal.responseEntity().contentType();
                }


                @Override
                public long contentLength() throws IOException
                {
                    return mOriginal.responseEntity().contentLength();
                }


                @Override
                public InputStream contentStream() throws IOException
                {
                    if (mIsGzipped)
                    {
                        return new ByteArrayInputStream(GZIPCompression.compress(mBody));
                    }
                    else
                    {
                        return new ByteArrayInputStream(mBody.getBytes("UTF-8"));
                    }
                }
            };
        }


        @Override
        public URI requestUri()
        {
            return mOriginal.requestUri();
        }


        @Override
        public URI responseUri()
        {
            return mOriginal.responseUri();
        }
    }


    // http://stackoverflow.com/a/35446009/4247460
    private static String inputStreamToString(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1)
        {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }


    // http://stackoverflow.com/a/3627442/4247460
    private static String gzipInputStreamToString(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int length;
        while ((length = inputStream.read(buffer)) != -1)
        {
            result.write(buffer, 0, length);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(result.toByteArray());
        GZIPInputStream gzis = new GZIPInputStream(bais);
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        StringBuffer sb = new StringBuffer();
        String readed;
        while ((readed = in.readLine()) != null)
        {
            sb.append(readed);
        }
        return sb.toString();
    }

}
