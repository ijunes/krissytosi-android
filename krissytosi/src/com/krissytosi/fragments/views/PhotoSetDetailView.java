/*
   Copyright 2012 - 2013 Sean O' Shea

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
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.github.espiandev.showcaseview.ShowcaseView;
import com.krissytosi.R;
import com.krissytosi.api.domain.Photo;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.fragments.adapters.ImagePagerAdapter;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.KrissyTosiUtils;
import com.krissytosi.utils.KrissyTosiUtils.ImageSize;
import com.viewpagerindicator.LinePageIndicator;

import java.util.List;

/**
 * View for a particular photo set. Allows a user to swipe through the photos in
 * a photo set.
 */
public class PhotoSetDetailView extends BaseDetailView {

    private static final String LOG_TAG = "PhotoSetDetailView";

    /**
     * PhotoSet which backs this view.
     */
    private PhotoSet photoSet;

    /**
     * Builds the page and kicks off the request to find all the images for this
     * photo set.
     */
    public void buildPage() {
        if (photoSet != null) {
            viewPager = (ViewPager) getBaseView().findViewById(R.id.pager);
            configureShowcaseView();
            List<Photo> photos = photoSet.getPhotos();
            String[] images = new String[photos.size()];
            int counter = 0;
            int maximumHeight = 0;
            int maximumWidth = 0;
            for (Photo photo : photos) {
                images[counter] = KrissyTosiUtils.determineImageUrl(photo, ImageSize.LARGE);
                if (photo.getWidthMedium() > maximumWidth) {
                    maximumWidth = photo.getWidthMedium();
                }
                if (photo.getHeightMedium() > maximumHeight) {
                    maximumHeight = photo.getHeightMedium();
                }
                counter++;
            }
            double allowedHeight = KrissyTosiUtils.getAllowedHeight(maximumHeight, maximumWidth,
                    (Activity) getContext());
            viewPager.setAdapter(new ImagePagerAdapter(images, ((Activity) getContext())
                    .getLayoutInflater(),
                    AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in),
                    getContext(), KrissyTosiConstants.FRAGMENT_PHOTOSETS_POSITION));
            viewPager.setCurrentItem(0);
            LinePageIndicator indicator = (LinePageIndicator) getBaseView()
                    .findViewById(R.id.photoset_view_indicator);
            indicator.setViewPager(viewPager);
            viewPager.setCurrentItem(0);
            indicator.setCurrentItem(0);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager
                    .getLayoutParams();
            params.height = (int) allowedHeight;
        }
    }

    private void configureShowcaseView() {
        if (getContext() != null) {
            Resources resources = getContext().getResources();
            ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
            co.hideOnClickOutside = true;
            ShowcaseView sv = ShowcaseView.insertShowcaseView(R.id.pager, (Activity) getContext(),
                    resources.getString(R.string.showcase_portfolio_header),
                    resources.getString(R.string.showcase_portfolio_runner), co);
            ;
        }
    }

    // Getters/Setters

    public PhotoSet getPhotoSet() {
        return photoSet;
    }

    public void setPhotoSet(PhotoSet photoSet) {
        this.photoSet = photoSet;
    }
}
