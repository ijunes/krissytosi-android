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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyCore.RequestManager;
import com.etsy.etsyModels.BaseModel;
import com.etsy.etsyModels.Listing;
import com.etsy.etsyRequests.ListingsRequest;
import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.fragments.adapters.StoreAdapter;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves listings for a store & displays them to the user.
 */
public class StoreFragment extends BaseListFragment {

    private static final String LOG_TAG = "StoreFragment";

    /**
     * Task used to retrieve the shop's listings from the API server.
     */
    private GetListingsTask getListingsTask;

    private ListView listView;
    private RelativeLayout detailView;

    /**
     * Adapter which backs this view.
     */
    private StoreAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.store, container, false);
        v.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (!isListViewShowing()) {
                        toggleListView(true);
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public String getFragmentIdentifier() {
        return KrissyTosiConstants.FRAGMENT_STORE_ID;
    }

    @Override
    public void onTabSelected() {
        if (getActivity() != null && getListingsTask == null && adapter == null) {
            // check to see whether we even need to get more store listings
            toggleLoading(true, getActivity().findViewById(android.R.id.list));
            getListingsTask = new GetListingsTask();
            getListingsTask.execute(((KrissyTosiApplication)
                    getActivity().getApplication()).getRequestManager());
        } else if (adapter != null && adapter.getCount() > 0) {
            toggleLoading(false, getActivity().findViewById(android.R.id.list));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getListingsTask != null) {
            getListingsTask.cancel(true);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        toggleListView(false);
    }

    /**
     * Checks to see whether the main store {@link ListView} is showing or not.
     * 
     * @return boolean indicating that the store view is showing.
     */
    public boolean isListViewShowing() {
        return listView != null && listView.getVisibility() != View.GONE;
    }

    /**
     * Given the listings parameter, this method is responsible for building out
     * the ListView of listings.
     * 
     * @param listings the listings retrieved from the API server.
     */
    protected void buildView(List<Listing> listings) {
        listView = (ListView) getActivity().findViewById(android.R.id.list);
        detailView = (RelativeLayout) getActivity().findViewById(R.id.detail_view);
        if (adapter == null) {
            adapter = new StoreAdapter(getActivity(), R.layout.store_listing,
                    (ArrayList<Listing>) listings);
        }
        if (getListAdapter() == null) {
            setListAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        toggleLoading(false, getActivity().findViewById(android.R.id.list));
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

    private void toggleListView(boolean show) {
        // TODO - animations?
        if (show) {
            detailView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
            detailView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Simple AsynTask to retrieve the listings for an Etsy shop.
     */
    private class GetListingsTask extends
            AsyncTask<RequestManager, Void, EtsyResult> {

        @Override
        protected EtsyResult doInBackground(RequestManager... params) {
            RequestManager requestManager = params[0];
            ListingsRequest request = ListingsRequest
                    .findAllShopListingsActive(ApiConstants.ETSY_STORE_ID, true);
            return requestManager.runRequest(request);
        }

        @Override
        protected void onPostExecute(EtsyResult result) {
            onGetListings(result);
        }
    }
}
