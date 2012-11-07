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

package com.krissytosi.fragments.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.etsyCore.EtsyRequestManager;
import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyModels.BaseModel;
import com.etsy.etsyModels.Listing;
import com.etsy.etsyModels.ListingImage;
import com.etsy.etsyRequests.ListingImagesRequest;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends ArrayAdapter<Listing> {

    private static final String LOG_TAG = "StoreAdapter";

    private final List<Listing> listings;

    public StoreAdapter(Context context, int textViewResourceId, ArrayList<Listing> listings) {
        super(context, textViewResourceId, listings);
        this.listings = listings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.store_listing, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.listingImageView = (ImageView) v.findViewById(R.id.listing_image);
            viewHolder.listingTitle = (TextView) v.findViewById(R.id.listing_title);
            viewHolder.listingPrice = (TextView) v.findViewById(R.id.listing_price);
            v.setTag(viewHolder);
        }
        return populate(v, position);
    }

    private View populate(View v, int position) {
        if (position < listings.size()) {
            final Listing listing = listings.get(position);
            if (listing != null) {
                final ViewHolder holder = (ViewHolder) v.getTag();
                if (holder != null) {
                    holder.listingTitle.setText(listing.getTitle());
                    holder.listingPrice.setText(String.format("%s %s", listing.getCurrencyCode(),
                            listing.getPrice()));
                    ListingImage[] images = listing.getImages();
                    if (images != null && images.length > 0) {
                        ListingImage image = images[0];
                        UrlImageViewHelper.setUrlDrawable(holder.listingImageView,
                                image.getUrl75x75());
                    } else {
                        // no images for this listing - need to request them
                        new GetListingImagesTask().execute(listing,
                                ((KrissyTosiApplication) v.getContext().getApplicationContext())
                                        .getRequestManager());
                    }
                }
            }
        }
        return v;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (listings != null) {
            count = listings.size();
        }
        return count;
    }

    protected void updateDataSetWithListingImages(List<ListingImage> images) {
        ListingImage image = images.get(0);
        int listingId = image.getListingId();
        // TODO - should I be using a map here instead of an array?
        // http://stackoverflow.com/questions/5234576/what-adapter-shall-i-use-to-use-hashmap-in-a-listview
        for (int i = 0, l = listings.size(); i < l; i++) {
            Listing listing = listings.get(i);
            if (listing.getListingId() == listingId) {
                Log.d(LOG_TAG, "Found a listing which corresponds to the listing id");
                break;
            }
        }
    }

    protected void onGetListingImages(EtsyResult result) {
        List<BaseModel> results = result.getResults();
        if (HttpStatus.SC_OK == result.getCode() && results.size() > 0) {
            List<ListingImage> listingImages = new ArrayList<ListingImage>();
            for (BaseModel imageResult : results) {
                listingImages.add((ListingImage) imageResult);
            }
            updateDataSetWithListingImages(listingImages);
        } else {
            onGetListingImagesFailure(result.getCode());
        }
    }

    protected void onGetListingImagesFailure(int errorCode) {
        Log.d(LOG_TAG, "Failed to get images for listing " + errorCode);
    }

    public static class ViewHolder {
        public ImageView listingImageView;
        public TextView listingTitle;
        public TextView listingPrice;
    }

    public class GetListingImagesTask extends AsyncTask<Object, Void, EtsyResult> {

        @Override
        protected EtsyResult doInBackground(Object... params) {
            Listing listing = (Listing) params[0];
            EtsyRequestManager requestManager = (EtsyRequestManager) params[1];
            ListingImagesRequest request = ListingImagesRequest.getImage_Listing(
                    String.valueOf(listing.getListingId()), "");
            return requestManager.runRequest(request);
        }

        @Override
        protected void onPostExecute(EtsyResult result) {
            onGetListingImages(result);
        }
    }
}
