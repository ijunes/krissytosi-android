/*
   Copyright 2012 Sean O' Shea

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.krissytosi.R;
import com.krissytosi.utils.Constants;

/**
 * Just contains a web view which points at cottage farm blogspot.
 */
public class BlogFragment extends BaseFragment {

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
    public void onStart() {
        super.onStart();
        toggleLoading(true, getWebView());
        getWebView().loadUrl(Constants.BLOG_URL);
    }

    @Override
    public void onStop() {
        super.onStop();
        getWebView().stopLoading();
    }

    public class MyWebViewClient extends WebViewClient {

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
            toggleLoading(false, getWebView());
        }
    }

    private WebView getWebView() {
        return (WebView) getActivity().findViewById(R.id.webview);
    }
}
