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

package com.krissytosi;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.StrictMode;

import com.etsy.etsyCore.EtsyRequestManager;
import com.krissytosi.api.ApiClient;
import com.krissytosi.modules.KrissyTosiModule;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import dagger.ObjectGraph;

/**
 * Extensions to the {@link Application} class to include specifics for this
 * app. Responsible for setting up the API client and initiating the call to
 * retrieve the list of portfolios from the API server.
 */
public class KrissyTosiApplication extends Application {

    /**
     * Basic log tag for the main application.
     */
    private static final String LOG_TAG = "KrissyTosiApplication";

    /**
     * Defines what {@link ApplicationMode} the application is in.
     */
    private ApplicationMode applicationMode = ApplicationMode.DEVELOP;

    /**
     * Used to interact with the API server.
     */
    private ApiClient apiClient;

    /**
     * Used to send user interaction details off to an analytics back end.
     */
    private Tracking tracking;

    /**
     * Used to make API requests to the Etsy API server.
     */
    private EtsyRequestManager requestManager;

    /**
     * Defines what mode the application is in. Different functionality is
     * available for different modes.
     */
    public enum ApplicationMode {
        DEVELOP, TEST, PROD
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeStrictMode();
        initializeModules();
        initializeImageLoader();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.getInstance().clearMemoryCache();
    }

    private void initializeModules() {
        ObjectGraph objectGraph = ObjectGraph.create(new KrissyTosiModule());
        // API first
        apiClient = objectGraph.get(ApiClient.class);
        apiClient.setBaseUrl(Constants.LOCAL_API_URL);
        // then the tracker implementation
        tracking = objectGraph.get(Tracking.class);
    }

    @TargetApi(11)
    private void initializeStrictMode() {
        if (applicationMode == ApplicationMode.DEVELOP) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }

    private void initializeImageLoader() {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }

    // Getters/Setters

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public EtsyRequestManager getRequestManager() {
        if (requestManager == null) {
            requestManager = new EtsyRequestManager(ApiConstants.ETSY_API_KEY,
                    ApiConstants.ETSY_API_SECRET, ApiConstants.ETSY_CALLBACK,
                    ApiConstants.ETSY_SCOPE);
        }
        return requestManager;
    }

    public void setRequestManager(EtsyRequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public ApplicationMode getApplicationMode() {
        return applicationMode;
    }

    public void setApplicationMode(ApplicationMode applicationMode) {
        this.applicationMode = applicationMode;
    }
}
