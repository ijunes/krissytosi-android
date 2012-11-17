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

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.etsy.etsyModels.Listing;
import com.etsy.etsyModels.ListingImage;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.krissytosi.utils.KrissyTosiUtils;
import com.krissytosi.utils.KrissyTosiUtils.ImageSize;

/**
 * Includes logic on how to deal with interactions on a store detail view.
 */
public class StoreDetailView implements OnClickListener {

    private static final String LOG_TAG = "StoreDetailView";

    private View baseView;
    private Listing listing;
    private Context context;

    private ViewFlipper detailViewFlipper;
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
        detailViewFlipper = (ViewFlipper) baseView.findViewById(R.id.detail_view_flipper);
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
        detailViewCreated.setText(String.valueOf(listing.getCreationTsz()));
        detailViewPrice.setText(listing.getPrice());
        detailViewQuantity.setText(String.valueOf(listing.getQuantity()));
        detailViewWhenMade.setText(listing.getWhenMade());
    }

    private void addImagesToFlipper(Listing listing, Context context) {
        detailViewFlipper.removeAllViews();
        ListingImage[] images = listing.getImages();
        for (ListingImage listingImage : images) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams vp =
                    new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(vp);
            UrlImageViewHelper.setUrlDrawable(imageView,
                    KrissyTosiUtils.determineImageUrl(listingImage, ImageSize.LARGE));
            detailViewFlipper.addView(imageView);
        }
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
