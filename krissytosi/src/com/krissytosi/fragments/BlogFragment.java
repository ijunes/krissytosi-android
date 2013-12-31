/*
   Copyright 2012 - 2014 Sean O' Shea

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.krissytosi.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.krissytosi.R;
import com.krissytosi.utils.KrissyTosiConstants;

/**
 * Just contains a web view which points at cottage farm blogspot.
 */
public class BlogFragment extends BaseFragment {

    private final static String LOG_TAG = "BlogFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.blog, container, false);
        WebView webView = (WebView) v.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        return v;
    }

    @Override
    public int getFragmentIdentifier() {
        return KrissyTosiConstants.FRAGMENT_BLOG_POSITION;
    }

    @Override
    public void onStop() {
        super.onStop();
        getWebView().stopLoading();
    }

    @Override
    public void onTabSelected() {
        toggleLoading(true, getWebView());
        getWebView().loadUrl(KrissyTosiConstants.BLOG_URL);
        FragmentHelper.setTitle(getActivity(), getResources().getString(R.string.app_name));
    }

    @Override
    public void onCurrentTabClicked() {
        super.onCurrentTabClicked();
        onTabSelected();
    }

    public class MyWebViewClient extends WebViewClient {

        private boolean hasNetwork = true;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.endsWith(".mp4")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                view.getContext().startActivity(intent);
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (hasNetwork) {
                toggleLoading(false, getWebView());
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d(LOG_TAG, "Received error while loading blog view " + description + " "
                    + errorCode);
            if (errorCode == ERROR_HOST_LOOKUP) {
                hasNetwork = false;
                toggleNoNetwork(true, getWebView());
            }
        }
    }

    private WebView getWebView() {
        return (WebView) getActivity().findViewById(R.id.webview);
    }
}
