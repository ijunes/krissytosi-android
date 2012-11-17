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
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.krissytosi.api.ApiClient;
import com.krissytosi.api.store.StoreApiClient;
import com.krissytosi.modules.KrissyTosiModule;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;
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
     * Used to make API requests to the store API server.
     */
    private StoreApiClient storeApiClient;

    /**
     * Used to send user interaction details off to an analytics back end.
     */
    private Tracking tracking;

    /**
     * Used to understand the device's network state.
     */
    private NetworkReceiver receiver;

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
        initializeLogging();
        initializeModules();
        initializeNetworkReceiver();
        initializeImageLoader();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    public void onTerminate() {
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
        super.onTerminate();
    }

    private void initializeLogging() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            StringBuilder builder = new StringBuilder("**** Version Information **** \n");
            builder.append("versionName = ");
            builder.append(packageInfo.versionName);
            builder.append("\n");
            builder.append("versionCode = ");
            builder.append(packageInfo.versionCode);
            builder.append("\n");
            builder.append("Device = ");
            builder.append(android.os.Build.DEVICE);
            builder.append("\n");
            builder.append("Model = ");
            builder.append(android.os.Build.MODEL);
            builder.append("\n");
            builder.append("Product = ");
            builder.append(android.os.Build.PRODUCT);
            Log.d(LOG_TAG, builder.toString());
        } catch (NameNotFoundException e) {
            Log.e(LOG_TAG, "initializeLogging getPackageInfo", e);
        }
    }

    private void initializeModules() {
        ObjectGraph objectGraph = ObjectGraph.create(new KrissyTosiModule());
        // API first
        apiClient = objectGraph.get(ApiClient.class);
        apiClient.setBaseUrl(ApiConstants.TEST_API_URL);
        // then the etsy API implementation
        storeApiClient = objectGraph.get(StoreApiClient.class);
        // finally, the tracker implementation
        tracking = objectGraph.get(Tracking.class);
        tracking.initialize(this, KrissyTosiConstants.TRACKING_KEY);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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

    private void initializeNetworkReceiver() {
        receiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
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

    public StoreApiClient getStoreApiClient() {
        return storeApiClient;
    }

    public void setStoreApiClient(StoreApiClient storeApiClient) {
        this.storeApiClient = storeApiClient;
    }

    public Tracking getTracking() {
        return tracking;
    }

    public void setTracking(Tracking tracking) {
        this.tracking = tracking;
    }

    public ApplicationMode getApplicationMode() {
        return applicationMode;
    }

    public void setApplicationMode(ApplicationMode applicationMode) {
        this.applicationMode = applicationMode;
    }
}
