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

package com.krissytosi.fragments.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.etsyModels.Listing;
import com.etsy.etsyModels.ListingImage;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.krissytosi.R;
import com.krissytosi.fragments.StoreFragment;
import com.krissytosi.utils.KrissyTosiUtils;
import com.krissytosi.utils.KrissyTosiUtils.ImageSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Backs the list which is displayed in the {@link StoreFragment}.
 */
public class StoreAdapter extends ArrayAdapter<Listing> {

    private static final String LOG_TAG = "StoreAdapter";

    /**
     * Data structure which backs this adapter.
     */
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
                    if (listing.getTitle() != null) {
                        holder.listingTitle.setText(Html.fromHtml(listing.getTitle()));
                    }
                    holder.listingPrice.setText(String.format("%s %s", listing.getCurrencyCode(),
                            listing.getPrice()));
                    ListingImage[] images = listing.getImages();
                    if (images != null && images.length > 0) {
                        ListingImage image = images[0];
                        UrlImageViewHelper.setUrlDrawable(holder.listingImageView,
                                KrissyTosiUtils.determineImageUrl(image, ImageSize.MEDIUM));
                    } else {
                        // no images for this listing? TODO - display dummy pic?
                        Log.d(LOG_TAG, "No image for listing " + listing.getListingId());
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

    // Getters/Setters

    public List<Listing> getListings() {
        return listings;
    }

    public static class ViewHolder {
        public ImageView listingImageView;
        public TextView listingTitle;
        public TextView listingPrice;
    }
}
