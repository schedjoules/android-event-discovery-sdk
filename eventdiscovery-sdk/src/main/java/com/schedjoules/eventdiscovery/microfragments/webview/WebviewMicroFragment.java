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

package com.schedjoules.eventdiscovery.microfragments.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.schedjoules.eventdiscovery.R;

import org.dmfs.android.microfragments.BasicMicroFragmentEnvironment;
import org.dmfs.android.microfragments.FragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentEnvironment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.transitions.BackTransition;

import java.net.URI;


/**
 * A {@link MicroFragment} to present a website within the context of the app.
 *
 * @author Marten Gajda
 */
public final class WebviewMicroFragment implements MicroFragment<URI>
{
    private final String mTitle;
    private final URI mUrl;


    public WebviewMicroFragment(@NonNull String title, @NonNull URI url)
    {
        mTitle = title;
        mUrl = url;
    }


    @NonNull
    @Override
    public String title(@NonNull Context context)
    {
        return mTitle;
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host)
    {
        Fragment result = new WebviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(MicroFragment.ARG_ENVIRONMENT, new BasicMicroFragmentEnvironment<>(this, host));
        result.setArguments(args);
        return result;
    }


    @NonNull
    @Override
    public URI parameters()
    {
        return mUrl;
    }


    @Override
    public boolean skipOnBack()
    {
        return false;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mTitle);
        dest.writeSerializable(mUrl);
    }


    public final static Parcelable.Creator<WebviewMicroFragment> CREATOR = new Parcelable.Creator<WebviewMicroFragment>()
    {
        @Override
        public WebviewMicroFragment createFromParcel(Parcel source)
        {
            return new WebviewMicroFragment(source.readString(), (URI) source.readSerializable());
        }


        @Override
        public WebviewMicroFragment[] newArray(int size)
        {
            return new WebviewMicroFragment[size];
        }
    };


    /**
     * A fragment that presents a website.
     */
    public static final class WebviewFragment extends Fragment implements View.OnKeyListener
    {
        private WebView mWebView;
        private MicroFragmentEnvironment<URI> mEnvironment;


        @SuppressLint("SetJavaScriptEnabled")
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View root = inflater.inflate(R.layout.schedjoules_fragment_webview, container, false);
            mEnvironment = new FragmentEnvironment<>(this);
            // create and configure the WebView
            mWebView = (WebView) root.findViewById(R.id.schedjoules_webview);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.setOnKeyListener(this);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.loadUrl(mEnvironment.microFragment().parameters().toASCIIString());

            Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
            toolbar.setTitle(mEnvironment.microFragment().title(getActivity()));

            toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mEnvironment.host().execute(getActivity(), new BackTransition());
                }
            });
            return root;
        }


        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if (keyCode == KeyEvent.KEYCODE_BACK)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    {
                        if (mWebView.canGoBack())
                        {
                            // user went back
                            mWebView.goBack();
                        }
                        else
                        {
                            // no more pages in the history, go up
                            mEnvironment.host().execute(getActivity(), new BackTransition());
                        }
                    }
                }
                return true;
            }
            return false;
        }
    }
}
