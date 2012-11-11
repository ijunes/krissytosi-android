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

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.domain.Portfolio;
import com.krissytosi.utils.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Just display *a* portfolio for now. Ideally, this should support multiple
 * portfolios.
 */
public class PortfoliosFragment extends BaseFragment {

    private static final String LOG_TAG = "PortfolioFragment";

    /**
     * Task used to retrieve the portfolios from the API server.
     */
    private GetPortfoliosTask getPortfoliosTask;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ViewPager pager;
    public DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.portfolios, container, false);
        options = new DisplayImageOptions.Builder()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        pager = (ViewPager) v.findViewById(R.id.pager);
        return v;
    }

    @Override
    protected String getFragmentIdentifier() {
        return Constants.FRAGMENT_PORTFOLIO_ID;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getPortfoliosTask != null) {
            getPortfoliosTask.cancel(true);
        }
    }

    @Override
    protected void onTabSelected() {
        super.onTabSelected();
        if (getActivity() != null && getPortfoliosTask == null) {
            toggleLoading(true, pager);
            getPortfoliosTask = new GetPortfoliosTask();
            getPortfoliosTask.execute(((KrissyTosiApplication) getActivity().getApplication())
                    .getApiClient());
        }
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private final String[] urls;

        ImagePagerAdapter(String[] urls) {
            this.urls = urls;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            if (container instanceof ViewPager && object instanceof View) {
                ((ViewPager) container).removeView((View) object);
            }
        }

        @Override
        public void finishUpdate(View container) {
            // TODO
        }

        @Override
        public int getCount() {
            return urls.length;
        }

        @Override
        public Object instantiateItem(View view, int position) {
            final View imageLayout = getActivity().getLayoutInflater()
                    .inflate(R.layout.image, null);
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

            imageLoader.displayImage(urls[position], imageView, options,

                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted() {
                            spinner.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(FailReason failReason) {
                            String message = null;
                            switch (failReason) {
                                case IO_ERROR:
                                    message = "Input/Output error";
                                    break;
                                case OUT_OF_MEMORY:
                                    message = "Out Of Memory error";
                                    break;
                                default:
                                    message = "Unknown error";
                                    break;
                            }
                            Log.e(LOG_TAG, "Failed to load portfolio image " + message);
                        }

                        @Override
                        public void onLoadingComplete(Bitmap loadedImage) {
                            spinner.setVisibility(View.GONE);
                            Animation anim = AnimationUtils.loadAnimation(getActivity(),
                                    android.R.anim.fade_in);
                            imageView.setAnimation(anim);
                            anim.start();
                        }

                        @Override
                        public void onLoadingCancelled() {
                            // Do nothing
                        }
                    });
            if (view instanceof ViewPager) {
                ((ViewPager) view).addView(imageLayout, 0);
            }
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }

    /**
     * Callback executed when the portfolios API response has returned.
     * 
     * @param portfolios the list of portfolios from the server (or a list of
     *            errors detailing why the portfolios were not available).
     */
    protected void onGetPortfolios(List<Portfolio> portfolios) {
        // check to see that we actually have *a* portfolio back from the API
        // server
        if (portfolios.size() > 0) {
            // check to see that the first portfolio isn't an error (is there a
            // better way to communicate these errors?)
            Portfolio portfolio = portfolios.get(0);
            if (portfolio.getErrorCode() != -1 && portfolio.getErrorDescription() == null) {
                Log.d(LOG_TAG, "Retrieved at least one portfolio from the server");
                String[] images = {
                        "http://tabletpcssource.com/wp-content/uploads/2011/05/android-logo.png",
                        "http://simpozia.com/pages/images/stories/windows-icon.png",
                        "https://si0.twimg.com/profile_images/1135218951/gmail_profile_icon3_normal.png"
                };
                pager.setAdapter(new ImagePagerAdapter(images));
                pager.setCurrentItem(0);
            } else {
                handlePortfolioApiError(portfolio);
            }
        } else {
            Portfolio portfolio = new Portfolio();
            portfolio.setErrorCode(Constants.NO_PORTFOLIOS);
            portfolio.setErrorDescription(Constants.NO_PORTFOLIOS_DESCRIPTION);
            handlePortfolioApiError(portfolio);
        }
    }

    /**
     * Callback executed when we fail to retrieve the portfolios from the API.
     * 
     * @param errorPortfolio
     */
    protected void handlePortfolioApiError(Portfolio errorPortfolio) {
        Log.d(LOG_TAG, "Portfolio Failure: " + errorPortfolio.getErrorDescription());
        toggleNoNetwork(true, pager);
    }

    /**
     * Simple AsynTask to retrieve the list of portfolios from the API server.
     */
    private class GetPortfoliosTask extends
            AsyncTask<ApiClient, Void, List<Portfolio>> {

        @Override
        protected List<Portfolio> doInBackground(ApiClient... params) {
            ApiClient apiClient = params[0];
            return apiClient.getPortfolioService().getPortfolios();
        }

        @Override
        protected void onPostExecute(List<Portfolio> result) {
            onGetPortfolios(result);
        }
    }
}
