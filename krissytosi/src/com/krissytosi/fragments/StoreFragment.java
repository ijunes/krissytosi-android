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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.etsyCore.EtsyRequestManager;
import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyRequests.ShopsRequest;
import com.krissytosi.R;
import com.krissytosi.utils.ApiConstants;

public class StoreFragment extends Fragment {

    /**
     * Task used to retrieve the portfolios from the API server.
     */
    private GetShopTask getShopTask;

    /**
     * Manager which is used to execute etsy API requests.
     */
    private EtsyRequestManager requestManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.store, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (requestManager == null) {
            requestManager = new EtsyRequestManager(ApiConstants.ETSY_API_KEY,
                    ApiConstants.ETSY_API_SECRET, ApiConstants.ETSY_CALLBACK,
                    ApiConstants.ETSY_SCOPE);
        }
        if (getShopTask == null) {
            getShopTask = new GetShopTask();
            getShopTask.execute();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getShopTask != null) {
            getShopTask.cancel(true);
        }
    }

    protected void onGetShop(EtsyResult result) {

    }

    /**
     * Simple AsynTask to retrieve the list of portfolios from the API server.
     */
    private class GetShopTask extends
            AsyncTask<Void, Void, EtsyResult> {

        @Override
        protected EtsyResult doInBackground(Void... params) {
            ShopsRequest request = ShopsRequest.getShop(ApiConstants.ETSY_STORE_ID);
            return requestManager.runRequest(request);
        }

        @Override
        protected void onPostExecute(EtsyResult result) {
            onGetShop(result);
        }
    }
}
