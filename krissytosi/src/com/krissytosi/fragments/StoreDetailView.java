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

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsy.etsyModels.Listing;
import com.etsy.etsyModels.ListingImage;
import com.krissytosi.R;
import com.krissytosi.R.id;
import com.krissytosi.fragments.adapters.ImagePagerAdapter;
import com.krissytosi.utils.KrissyTosiUtils;
import com.krissytosi.utils.KrissyTosiUtils.ImageSize;

import java.util.Date;

/**
 * Includes logic on how to deal with interactions on a store detail view.
 */
public class StoreDetailView implements OnClickListener {

    private static final String LOG_TAG = "StoreDetailView";

    private View baseView;
    private Listing listing;
    private Context context;

    private ViewPager detailViewPager;
    private Button detailViewBuyButton;
    private TextView detailViewDescription;
    private TextView detailViewTitle;
    private TextView detailViewCreated;
    private TextView detailViewPrice;
    private TextView detailViewQuantity;
    private TextView detailViewWhenMade;

    @Override
    public void onClick(View v) {
        // to be sure
        if (v.equals(detailViewBuyButton)) {
            Log.d(LOG_TAG, "Will open " + listing.getUrl());
        }
    }

    public void buildPage() {
        detailViewPager = (ViewPager) baseView.findViewById(R.id.detail_view_pager);
        detailViewBuyButton = (Button) baseView.findViewById(R.id.detail_view_buy_button);
        detailViewTitle = (TextView) baseView.findViewById(R.id.detail_view_title);
        detailViewDescription = (TextView) baseView.findViewById(
                R.id.detail_view_description);
        detailViewCreated = (TextView) baseView.findViewById(R.id.detail_view_created);
        detailViewPrice = (TextView) baseView.findViewById(R.id.detail_view_price);
        detailViewQuantity = (TextView) baseView.findViewById(R.id.detail_view_quantity);
        detailViewWhenMade = (TextView) baseView.findViewById(R.id.detail_view_when_made);
        addImagesToFlipper(listing, context);
        detailViewBuyButton.setOnClickListener(this);
        detailViewTitle.setText(listing.getTitle());
        // descriptions can include \n's which should be translated into <br />s
        String detailDescription = listing.getDescription();
        detailDescription = detailDescription.replaceAll("\n", "<br />");
        detailViewDescription.setText(Html.fromHtml(detailDescription));
        Date listingCreated = new Date((long) listing.getCreationTsz() * 1000);
        detailViewCreated.setText(DateFormat.getDateFormat(context).format(listingCreated));
        detailViewPrice.setText(listing.getPrice());
        detailViewQuantity.setText(String.valueOf(listing.getQuantity()));
        detailViewWhenMade.setText(listing.getWhenMade());
    }

    private void addImagesToFlipper(Listing listing, Context context) {
        String[] images = new String[listing.getImages().length];
        int counter = 0;
        int fullHeight = 0;
        for (ListingImage listingImage : listing.getImages()) {
            int listingHeight = listingImage.getFullHeight();
            if (listingHeight > fullHeight) {
                fullHeight = listingHeight;
            }
            images[counter] = KrissyTosiUtils.determineImageUrl(listingImage, ImageSize.LARGE);
            counter++;
        }
        detailViewPager.setAdapter(new ImagePagerAdapter(images, (Activity) context));
        detailViewPager.setCurrentItem(0);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) detailViewPager
                .getLayoutParams();
        params.height = fullHeight;
        detailViewPager.setLayoutParams(params);
    }

    // Getters/Setters

    public View getBaseView() {
        return baseView;
    }

    public void setBaseView(View baseView) {
        this.baseView = baseView;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
