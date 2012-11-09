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
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.etsy.etsyCore.EtsyRequestManager;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.domain.Portfolio;
import com.krissytosi.modules.KrissyTosiModule;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import dagger.ObjectGraph;

import java.util.List;

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
     * Task used to retrieve the portfolios from the API server.
     */
    private GetPortfoliosTask getPortfoliosTask;

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
        getPortfoliosTask = new GetPortfoliosTask();
        getPortfoliosTask.execute();
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

    /**
     * Callback executed when the portfolios API response has returned.
     * 
     * @param portfolios the list of portfolios from the server (or a list of
     *            errors detailing why the portfolios were not available).
     */
    protected void onGetPortfolios(List<Portfolio> portfolios) {
        // check to see that we actually have *a* portfolio back from the API
        // server
        if (portfolios.size() > 0) {
            // check to see that the first portfolio isn't an error (is there a
            // better way to communicate these errors?)
            Portfolio portfolio = portfolios.get(0);
            if (portfolio.getErrorCode() != -1 && portfolio.getErrorDescription() == null) {
                Log.d(LOG_TAG, "Retrieved at least one portfolio from the server");
            } else {
                handlePortfolioApiError(portfolio);
            }
        } else {
            Portfolio portfolio = new Portfolio();
            portfolio.setErrorCode(Constants.NO_PORTFOLIOS);
            portfolio.setErrorDescription(Constants.NO_PORTFOLIOS_DESCRIPTION);
            handlePortfolioApiError(portfolio);
        }
    }

    protected void handlePortfolioApiError(Portfolio errorPortfolio) {
        // TODO - communicate this to the rest of the app.
        Log.d(LOG_TAG, "Portfolio Failure: " + errorPortfolio.getErrorDescription());
    }

    /**
     * Simple AsynTask to retrieve the list of portfolios from the API server.
     */
    private class GetPortfoliosTask extends
            AsyncTask<Void, Void, List<Portfolio>> {

        @Override
        protected List<Portfolio> doInBackground(Void... params) {
            return apiClient.getPortfolioService().getPortfolios();
        }

        @Override
        protected void onPostExecute(List<Portfolio> result) {
            onGetPortfolios(result);
        }
    }

    @Override
    public void onTerminate() {
        if (getPortfoliosTask != null) {
            getPortfoliosTask.cancel(true);
        }
        super.onTerminate();
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
