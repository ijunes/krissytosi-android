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

import android.support.v4.view.ViewPager;

import com.krissytosi.R;
import com.krissytosi.api.domain.Portfolio;

/**
 * View for a particular portfolio. Allows a user to swipe through the photos in
 * a portfolio.
 */
public class PortfolioDetailView extends BaseDetailView {

    private static final String LOG_TAG = "PortfolioDetailView";

    /**
     * Portfolio which backs this view.
     */
    private Portfolio portfolio;

    private ViewPager portfolioViewPager;

    public void buildPage() {
        portfolioViewPager = (ViewPager) getBaseView().findViewById(R.id.pager);
        // portfolioViewPager.setAdapter(new ImagePagerAdapter(images,
        // (Activity) getContext()));
        // portfolioViewPager.setCurrentItem(0);
    }

    // Getters/Setters

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
