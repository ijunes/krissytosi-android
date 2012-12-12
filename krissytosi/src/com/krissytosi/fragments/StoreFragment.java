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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyModels.BaseModel;
import com.etsy.etsyModels.Listing;
import com.etsy.etsyRequests.ListingsRequest;
import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.api.store.StoreApiClient;
import com.krissytosi.fragments.adapters.StoreAdapter;
import com.krissytosi.fragments.views.StoreDetailView;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.OnSwipeTouchListener;

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

    /**
     * ListView which is responsible for rendering the store.
     */
    private ListView listView;

    /**
     * Adapter which backs this view.
     */
    private StoreAdapter adapter;

    /**
     * View for handling events related to the detail store view.
     */
    private StoreDetailView storeDetailView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.store, container, false);
        ScrollView storeDetailView = (ScrollView) v.findViewById(R.id.store_detail_view);
        storeDetailView.setSmoothScrollingEnabled(true);
        storeDetailView.setOnTouchListener(new OnSwipeTouchListener() {

            @Override
            public void onSwipeRight() {
                toggleListView(true);
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
            toggleLoading(true, getActivity().findViewById(R.id.store_flipper));
            getListingsTask = new GetListingsTask();
            getListingsTask.execute(((KrissyTosiApplication)
                    getActivity().getApplication()).getStoreApiClient());
        } else if (adapter != null && adapter.getCount() > 0) {
            toggleLoading(false, getActivity().findViewById(R.id.store_flipper));
        }
    }

    @Override
    public void onCurrentTabClicked() {
        super.onCurrentTabClicked();
        if (!isListViewShowing()) {
            toggleListView(true);
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
        Listing listing = adapter.getItem(position);
        populateStoreListing(listing);
        getView().findViewById(R.id.store_detail_view).setVisibility(View.VISIBLE);
    }

    /**
     * Checks to see whether the main store {@link ListView} is showing or not.
     * 
     * @return boolean indicating that the store view is showing.
     */
    public boolean isListViewShowing() {
        return listView != null && listView.getVisibility() != View.GONE;
    }

    @Override
    public void onPhotoLoaded(int height, int width) {
        super.onPhotoLoaded(height, width);
        // when a photo is loaded into a ImagePagerAdapter, the storeDetailView
        // needs to be notified in order to resize itself to accommodate the
        // image.
        if (!isListViewShowing()) {
            storeDetailView.onPhotoLoaded(height, width);
        }
    }

    /**
     * Given the listings parameter, this method is responsible for building out
     * the ListView of listings.
     * 
     * @param listings the listings retrieved from the API server.
     */
    protected void buildView(List<Listing> listings) {
        flipperId = R.id.store_flipper;
        listView = (ListView) getActivity().findViewById(android.R.id.list);
        if (adapter == null) {
            adapter = new StoreAdapter(getActivity(), R.layout.store_listing,
                    (ArrayList<Listing>) listings);
        }
        if (getListAdapter() == null) {
            setListAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        toggleLoading(false, getActivity().findViewById(R.id.store_flipper));
    }

    /**
     * Executed when we've figured out the listings for the store.
     * 
     * @param result should contain all the shop listings
     */
    protected void onGetListings(EtsyResult result) {
        if (result != null) {
            List<BaseModel> results = result.getResults();
            if (HttpStatus.SC_OK == result.getCode()) {
                List<Listing> listings = new ArrayList<Listing>();
                for (BaseModel listingResult : results) {
                    listings.add((Listing) listingResult);
                }
                buildView(listings);
            } else {
                onGetListingsFailure(result.getCode());
            }
        } else {
            onGetListingsFailure(HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
     * Hides/shows the store list view based on the show parameter.
     * 
     * @param show boolean indicating that the list should be visible.
     */
    private void toggleListView(boolean show) {
        FragmentHelper.toggleFlipper(show,
                (ViewFlipper) getView().findViewById(R.id.store_flipper),
                getActivity(), getFragmentIdentifier());
        if (!show) {
            getView().findViewById(R.id.store_detail_view).scrollTo(0, 0);
        }
    }

    /**
     * Executed when a user clicks on a particular store listing.
     * 
     * @param listing the listing on which the user clicked.
     */
    private void populateStoreListing(Listing listing) {
        // perhaps pop these into member variables
        if (storeDetailView == null) {
            storeDetailView = new StoreDetailView();
        }
        storeDetailView.setBaseView(getView());
        storeDetailView.setListing(listing);
        storeDetailView.setContext(getActivity());
        storeDetailView.buildPage();
    }

    /**
     * Simple AsynTask to retrieve the listings for an Etsy shop.
     */
    private class GetListingsTask extends
            AsyncTask<StoreApiClient, Void, EtsyResult> {

        @Override
        protected EtsyResult doInBackground(StoreApiClient... params) {
            StoreApiClient storeApiClient = params[0];
            ListingsRequest request = ListingsRequest
                    .findAllShopListingsActive(ApiConstants.ETSY_STORE_ID, true);
            return storeApiClient.runRequest(request);
        }

        @Override
        protected void onPostExecute(EtsyResult result) {
            onGetListings(result);
        }
    }
}
