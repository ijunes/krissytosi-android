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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.etsy.etsyCore.EtsyRequestManager;
import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyModels.BaseModel;
import com.etsy.etsyModels.Listing;
import com.etsy.etsyRequests.ListingsRequest;
import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.fragments.adapters.StoreAdapter;
import com.krissytosi.utils.ApiConstants;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves listings for a store & displays them to the user.
 */
public class StoreFragment extends Fragment {

    private static final String LOG_TAG = "StoreFragment";

    /**
     * Task used to retrieve the shop's listings from the API server.
     */
    private GetListingsTask getListingsTask;

    private ListView listView;
    private StoreAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.store, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getListingsTask == null) {
            getListingsTask = new GetListingsTask();
            getListingsTask.execute(((KrissyTosiApplication) getActivity().getApplication())
                    .getRequestManager());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getListingsTask != null) {
            getListingsTask.cancel(true);
        }
    }

    /**
     * Given the listings parameter, this method is responsible for building out
     * the ListView of listings.
     * 
     * @param listings the listings retrieved from the API server.
     */
    protected void buildView(List<Listing> listings) {
        if (adapter == null) {
            adapter = new StoreAdapter(getActivity(), R.layout.store_listing,
                    (ArrayList<Listing>) listings);
        }
        if (listView == null) {
            listView = (ListView) getView().findViewById(R.id.listings);
        }
        if (listView.getAdapter() == null) {
            listView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Executed when we've figured out the listings for the store.
     * 
     * @param result should contain all the shop listings
     */
    protected void onGetListings(EtsyResult result) {
        List<BaseModel> results = result.getResults();
        if (HttpStatus.SC_OK == result.getCode()) {
            Log.d(LOG_TAG, "Got some listings back from the API server");
            List<Listing> listings = new ArrayList<Listing>();
            for (BaseModel listingResult : results) {
                listings.add((Listing) listingResult);
            }
            buildView(listings);
        } else {
            onGetListingsFailure(result.getCode());
        }
    }

    /**
     * Executed when something went awry with the API call to retrieve the
     * listings.
     * 
     * @param errorCode the HTTP status error code returned from the API server.
     */
    protected void onGetListingsFailure(int errorCode) {
        Log.d(LOG_TAG, "Failed to get listings " + errorCode);
    }

    /**
     * Simple AsynTask to retrieve the listings for an Etsy shop.
     */
    private class GetListingsTask extends
            AsyncTask<EtsyRequestManager, Void, EtsyResult> {

        @Override
        protected EtsyResult doInBackground(EtsyRequestManager... params) {
            EtsyRequestManager requestManager = params[0];
            ListingsRequest request = ListingsRequest
                    .findAllShopListingsActive(ApiConstants.ETSY_STORE_ID);
            return requestManager.runRequest(request);
        }

        @Override
        protected void onPostExecute(EtsyResult result) {
            onGetListings(result);
        }
    }
}
