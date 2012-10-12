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

import java.util.List;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.krissytosi.api.ApiClient;
import com.krissytosi.api.NetworkedApiClient;
import com.krissytosi.api.domain.Portfolio;
import com.krissytosi.utils.Constants;

/**
 * Extensions to the {@link Application} class to include specifics for this
 * app. Responsible for setting up the API client and initiating the call to
 * retrieve the list of portfolios from the API server.
 */
public class KrissyTosiApplication extends Application {

	private static final String LOG_TAG = "KrissyTosiApplication";

	/**
	 * Used to interact with the API server.
	 */
	private ApiClient apiClient;

	/**
	 * Task used to retrieve the portfolios from the API server.
	 */
	private GetPortfoliosTask getPortfoliosTask;

	@Override
	public void onCreate() {
		apiClient = new NetworkedApiClient();
		apiClient.setBaseUrl(Constants.TEST_API_URL);
		getPortfoliosTask = new GetPortfoliosTask();
		getPortfoliosTask.execute();
		super.onCreate();
	}

	protected void onGetPortfolios(List<Portfolio> portfolios) {
		// check to see that we actually have *a* portfolio back from the API
		// server
		if (portfolios.size() > 0) {
			// tell everyone else that we have the list of portfolios back from
			// the server
			// TODO
		} else {
			Log.d(LOG_TAG, "Failed to retrieve portfolios from the API server");
		}
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

	// Getters/Setters

	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}
}
