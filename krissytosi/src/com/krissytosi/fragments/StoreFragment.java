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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyModels.BaseModel;
import com.etsy.etsyModels.Listing;
import com.etsy.etsyModels.Shop;
import com.etsy.etsyRequests.ListingsRequest;
import com.etsy.etsyRequests.ShopsRequest;
import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.api.store.ParcelableListing;
import com.krissytosi.api.store.StoreApiClient;
import com.krissytosi.fragments.adapters.StoreAdapter;
import com.krissytosi.fragments.views.StoreDetailView;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.OnSwipeTouchListener;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves listings for a store & displays them to the user.
 */
public class StoreFragment extends BaseListFragment {

    private static final String LOG_TAG = "StoreFragment";

    // orientation keys

    private static final String LISTINGS = "com.krissytosi.fragments.StoreFragment.LISTINGS";
    private static final String VACATION_MESSAGE = "com.krissytosi.fragments.StoreFragment.VACATION_MESSAGE";
    private static final String CURRENT_LISTING_POSITION = "com.krissytosi.fragments.StoreFragment.CURRENT_LISTING_POSITION";
    private static final int CURRENT_LISTING_POSITION_DEFAULT_VALUE = -1;
    private int currentListingPosition = CURRENT_LISTING_POSITION_DEFAULT_VALUE;

    /**
     * Task used to retrieve the shop's listings from the API server.
     */
    private GetListingsTask getListingsTask;

    /**
     * Async task used to get particulars on the current store. Mostly a
     * fire-and-forget type of request as it's not *absolutely* necessary to let
     * the user know that the store is currently on vacation in the application
     * proper.
     */
    private GetShopTask getShopTask;

    /**
     * ListView which is responsible for rendering the store.
     */
    private ListView listView;

    /**
     * Adapter which backs this view.
     */
    private StoreAdapter adapter;

    /**
     * Message displayed to the user should the shop be on vacation.
     */
    private String vacationMessage;

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
        if (savedInstanceState != null) {
            currentListingPosition = savedInstanceState.getInt(CURRENT_LISTING_POSITION,
                    CURRENT_LISTING_POSITION_DEFAULT_VALUE);
            // see if we had already retrieved the listings
            if (savedInstanceState.containsKey(LISTINGS)) {
                // get them back from the bundle
                ArrayList<ParcelableListing> parcelableListings = savedInstanceState
                        .getParcelableArrayList(LISTINGS);
                // convert them into a usable list
                List<Listing> listings = parseListingsFromParcelableListings(parcelableListings);
                adapter = new StoreAdapter(getActivity(), R.layout.store_listing,
                        (ArrayList<Listing>) listings);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            // check to see whether we've already requested the store vacation
            // information
            if (savedInstanceState.containsKey(VACATION_MESSAGE)) {
                vacationMessage = savedInstanceState.getString(VACATION_MESSAGE);
            }
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // ensure that the correct detail view shows up again after an
        // orientation change
        if (!isListViewShowing()) {
            outState.putInt(CURRENT_LISTING_POSITION, currentListingPosition);
        }
        // save the current collection of listings if we've already gone to the
        // trouble to get them from the API server.
        if (hasListings()) {
            outState.putParcelableArrayList(LISTINGS,
                    createParcelableListingsFromListings(adapter.getListings()));
        }
        if (vacationMessage != null) {
            outState.putString(VACATION_MESSAGE, vacationMessage);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getFragmentIdentifier() {
        return KrissyTosiConstants.FRAGMENT_STORE_POSITION;
    }

    @Override
    public void onTabSelected() {
        FragmentHelper.setTitle(getActivity(), getResources().getString(R.string.app_name));
        if (getActivity() != null && getListingsTask == null) {
            // check to see whether we even need to get more store listings
            if (hasListings()) {
                // they've been retrieved already ...
                currentListingPosition = CURRENT_LISTING_POSITION_DEFAULT_VALUE;
                buildView(adapter.getListings());
            } else {
                // no listings yet ... go get 'em
                toggleLoading(true, getActivity().findViewById(R.id.store_flipper));
                getListingsTask = new GetListingsTask();
                getListingsTask.execute(((KrissyTosiApplication)
                        getActivity().getApplication()).getStoreApiClient());
            }
        } else if (hasListings()) {
            toggleLoading(false, getActivity().findViewById(R.id.store_flipper));
            if (currentListingPosition != CURRENT_LISTING_POSITION_DEFAULT_VALUE
                    && adapter.getCount() > currentListingPosition) {
                handleOnListItemClick(currentListingPosition);
                storeDetailView.setMaximumHeight(0);
            }
        }
    }

    @Override
    public void onCurrentTabClicked() {
        if (!isListViewShowing()) {
            toggleListView(true);
            currentListingPosition = CURRENT_LISTING_POSITION_DEFAULT_VALUE;
            FragmentHelper.handleDetailViewBeforeDetatched(storeDetailView);
        }
    }

    @Override
    public void beforeDetatched() {
        super.beforeDetatched();
        FragmentHelper.handleDetailViewBeforeDetatched(storeDetailView);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getListingsTask != null) {
            getListingsTask.cancel(true);
        }
        if (getShopTask != null) {
            getShopTask.cancel(true);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        handleOnListItemClick(position);
    }

    @Override
    public void onLongPressDetected() {
        super.onLongPressDetected();
        if (longPressDetected()) {
            getView().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
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
        // when a photo is loaded into a ImagePagerAdapter, the storeDetailView
        // needs to be notified in order to resize itself to accommodate the
        // image.
        if (hasStoreDetailView()) {
            storeDetailView.onPhotoLoaded(height, width);
        }
    }

    private boolean hasListings() {
        return adapter != null && adapter.getCount() > 0;
    }

    private boolean hasStoreDetailView() {
        return !isListViewShowing() && storeDetailView != null;
    }

    private boolean longPressDetected() {
        boolean longPressDetected = false;
        if (hasStoreDetailView() && storeDetailView.getListing() != null) {
            Intent intent = FragmentHelper.createShareUrlIntent(storeDetailView.getListing()
                    .getUrl());
            StoreFragment.this.startActivity(Intent.createChooser(intent, getResources()
                    .getString(R.string.share_this)));
        }
        return longPressDetected;
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
        // once we've retrieved the listings and built the list view, check to
        // see whether we need to get the store details.
        if (vacationMessage == null && getShopTask == null) {
            getShopTask = new GetShopTask();
            getShopTask.execute(((KrissyTosiApplication)
                    getActivity().getApplication()).getStoreApiClient());
        }
    }

    /**
     * Given the shop parameter, this method is responsible for checking to see
     * whether the shop is currently on vacation and updating the view
     * accordingly.
     * 
     * @param shop the shop returned from the API server.
     */
    protected void updateViewWithStoreDetails(Shop shop) {
        vacationMessage = "";
        if (shop.getIsVacation()) {
            if (shop.getVacationMessage() != null) {
                vacationMessage = shop.getVacationMessage();
                Crouton.makeText(getActivity(), vacationMessage, Style.INFO).show();
            }
        } else {
            Log.d(LOG_TAG, "Store is not on vacation");
        }
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
        getListingsTask = null;
    }

    /**
     * Callback executed when we get the store details back from the server.
     * 
     * @param result the etsy response from the server.
     */
    protected void onGetShop(EtsyResult result) {
        if (result != null) {
            List<BaseModel> results = result.getResults();
            if (HttpStatus.SC_OK == result.getCode()) {
                if (results.size() > 0) {
                    Shop shop = (Shop) results.get(0);
                    updateViewWithStoreDetails(shop);
                } else {
                    // TODO - can this even happen?
                    onGetShopFailure(result.getCode());
                }
            } else {
                onGetShopFailure(result.getCode());
            }
        } else {
            onGetShopFailure(HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
        checkForNetworkFailure(errorCode);
    }

    /**
     * Callback executed should an error occur while retrieving the store
     * details from the API server.
     * 
     * @param errorCode the HTTP error code returned from the API server.
     */
    protected void onGetShopFailure(int errorCode) {
        Log.d(LOG_TAG, "Failed to get store details " + errorCode);
        checkForNetworkFailure(errorCode);

    }

    /**
     * Checks to see whether the error code returned from the async task
     * warrants a 'You need to connect to the internet' message.
     * 
     * @param errorCode the error code returned from the async task.
     */
    private void checkForNetworkFailure(int errorCode) {
        switch (errorCode) {
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
            case HttpStatus.SC_NOT_FOUND:
                toggleNoNetwork(true, listView);
                break;
            default:
                break;
        }
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
        } else {
            getView().findViewById(android.R.id.list).setVisibility(View.VISIBLE);
            currentListingPosition = CURRENT_LISTING_POSITION_DEFAULT_VALUE;
            FragmentHelper.setTitle(getActivity(), getResources().getString(R.string.app_name));
        }
    }

    /**
     * Executed when a user clicks on a particular store listing.
     * 
     * @param listing the listing on which the user clicked.
     */
    private void populateStoreListing(Listing listing) {
        if (storeDetailView == null) {
            storeDetailView = new StoreDetailView();
        }
        storeDetailView.setBaseView(getView());
        storeDetailView.setListing(listing);
        storeDetailView.setContext(getActivity());
        storeDetailView.buildPage();
    }

    /**
     * Executes behavior necessary when a user clicks on a particular store
     * item.
     * 
     * @param position the position in the list view on which the user clicked.
     */
    private void handleOnListItemClick(int position) {
        toggleListView(false);
        Listing listing = adapter.getItem(position);
        populateStoreListing(listing);
        getView().findViewById(R.id.store_detail_view).setVisibility(View.VISIBLE);
        currentListingPosition = position;
        FragmentHelper.setTitle(getActivity(), listing.getTitle());
    }

    /**
     * Given a collection of {@link ParcelableListing} listings, this method
     * returns a list of {@link Listing} objects.
     * 
     * @param parcelableListings retrieved from the saved instance state.
     * @return a usable List of {@link Listing} objects.
     */
    private List<Listing> parseListingsFromParcelableListings(
            ArrayList<ParcelableListing> parcelableListings) {
        List<Listing> listings = new ArrayList<Listing>();
        for (ParcelableListing parcelableListing : parcelableListings) {
            listings.add(parcelableListing.getListing());
        }
        return listings;
    }

    /**
     * Given a collection of {@link Listing} listings, this method returns an
     * ArrayList of {@link ParcelableListing} objects.
     * 
     * @param adapterListings
     * @return
     */
    private ArrayList<ParcelableListing> createParcelableListingsFromListings(
            List<Listing> adapterListings) {
        ArrayList<ParcelableListing> parcelableListings = new ArrayList<ParcelableListing>();
        for (Listing listing : adapterListings) {
            ParcelableListing parcelableListing = new ParcelableListing();
            parcelableListing.setListing(listing);
            parcelableListings.add(parcelableListing);
        }
        return parcelableListings;
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

    /**
     * Async task for retrieving specifics on the store. Retrieves information
     * on whether or not the shop is on vacation or if there's any specific shop
     * announcement.
     */
    private class GetShopTask extends
            AsyncTask<StoreApiClient, Void, EtsyResult> {

        @Override
        protected EtsyResult doInBackground(StoreApiClient... params) {
            StoreApiClient storeApiClient = params[0];
            ShopsRequest request = ShopsRequest.getShop(ApiConstants.ETSY_STORE_ID);
            return storeApiClient.runRequest(request);
        }

        @Override
        protected void onPostExecute(EtsyResult result) {
            onGetShop(result);
        }
    }
}
