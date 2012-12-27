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

package com.krissytosi.fragments.views;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsy.etsyModels.Listing;
import com.etsy.etsyModels.ListingImage;
import com.krissytosi.R;
import com.krissytosi.fragments.adapters.ImagePagerAdapter;
import com.krissytosi.utils.KrissyTosiUtils;
import com.krissytosi.utils.KrissyTosiUtils.ImageSize;

/**
 * Includes logic on how to deal with interactions on a store detail view.
 */
public class StoreDetailView extends BaseDetailView implements OnClickListener,
        OnPageChangeListener {

    private static final String LOG_TAG = "StoreDetailView";

    private int maximumHeight;
    private Listing listing;

    private TextView detailViewPagerIndicator;
    private TextView detailViewPrice;
    private Button detailViewBuyButton;
    private TextView detailViewDescription;
    private TextView detailViewQuantity;
    private TextView detailViewWhenMade;

    @Override
    public void onClick(View v) {
        // to be sure
        if (v.equals(detailViewBuyButton)) {
            Log.d(LOG_TAG, "Will open " + listing.getUrl());
        }
    }

    @Override
    public void beforeDetatched() {
        super.beforeDetatched();
        if (detailViewBuyButton != null) {
            detailViewBuyButton.setOnClickListener(null);
        }
    }

    public void buildPage() {
        // reset the max height
        maximumHeight = 0;
        // first get all the views back from the base view
        viewPager = (ViewPager) getBaseView().findViewById(R.id.detail_view_pager);
        detailViewPagerIndicator = (TextView) getBaseView().findViewById(
                R.id.detail_view_pager_indicator);
        detailViewPrice = (TextView) getBaseView().findViewById(R.id.detail_view_price);
        detailViewBuyButton = (Button) getBaseView().findViewById(R.id.detail_view_buy_button);
        detailViewDescription = (TextView) getBaseView().findViewById(
                R.id.detail_view_description);
        detailViewQuantity = (TextView) getBaseView().findViewById(R.id.detail_view_quantity);
        detailViewWhenMade = (TextView) getBaseView().findViewById(R.id.detail_view_when_made);
        // then assign values & text to each view.
        Resources resources = getContext().getResources();
        initializeViewPager();
        // descriptions can include \n's which should be translated into <br />s
        String detailDescription = listing.getDescription();
        detailDescription = detailDescription.replaceAll("\n", "<br />");
        detailViewDescription.setText(Html.fromHtml(detailDescription));
        detailViewPagerIndicator.setText(String.format(
                resources.getString(R.string.detail_view_num_images), 1,
                listing.getImages().length));
        // TODO - i18n & placement of currency symbols
        detailViewPrice.setText(String.format("%s %s", listing.getCurrencyCode(),
                listing.getPrice()));
        detailViewBuyButton.setOnClickListener(this);
        String quantity = "";
        String quantityResource = resources.getString(R.string.detail_view_quantity);
        int listingQuantity = listing.getQuantity();
        if (listingQuantity == 1) {
            quantity = String.format(quantityResource,
                    resources.getString(R.string.detail_view_quantity_one));
        } else {
            quantity = String.format(quantityResource, String.format(
                    resources.getString(R.string.detail_view_quantity_n), listingQuantity));
        }
        detailViewQuantity.setText(quantity);
        detailViewWhenMade.setText(String.format(resources.getString(R.string.detail_view_made),
                listing.getWhenMade()));
    }

    public void onPhotoLoaded(int height, int width) {
        // first check the device's orientation to see whether the height and
        // width requested can even be accommodated.
        if (getContext() != null) {
            double requestedHeight = height;
            boolean isPortrait = getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            if (isPortrait) {
                // check to see whether the requested image width will be
                // automatically squashed
                if (metrics.widthPixels < width) {
                    // this will have an impact on the height of the image.
                    double difference = width - metrics.widthPixels;
                    double percentage = width / difference;
                    requestedHeight = height / percentage;
                }
            } else {
                // TODO
            }
            if (requestedHeight > maximumHeight) {
                maximumHeight = (int) requestedHeight;
                // TODO - animate
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager
                                .getLayoutParams();
                        params.height = maximumHeight;
                    }
                });
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        detailViewPagerIndicator.setText(String.format(
                getContext().getResources().getString(R.string.detail_view_num_images),
                position + 1,
                listing.getImages().length));
    }

    private void initializeViewPager() {
        String[] images = new String[listing.getImages().length];
        int counter = 0;
        for (ListingImage listingImage : listing.getImages()) {
            images[counter] = KrissyTosiUtils.determineImageUrl(listingImage, ImageSize.LARGE);
            counter++;
        }
        viewPager.setAdapter(new ImagePagerAdapter(images, ((Activity) getContext())
                .getLayoutInflater(), AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in), getContext()));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(this);
    }

    // Getters/Setters

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }
}
