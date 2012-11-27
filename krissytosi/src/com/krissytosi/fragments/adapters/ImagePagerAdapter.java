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

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.krissytosi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Used to show a list of swipe-able images.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private static final String LOG_TAG = "ImagePagerAdapter";

    private final Activity activity;
    private final String[] urls;
    private final DisplayImageOptions options;

    public ImagePagerAdapter(String[] urls, Activity activity) {
        this.urls = urls;
        this.options = new DisplayImageOptions.Builder()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        this.activity = activity;
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
        final View imageLayout = activity.getLayoutInflater().inflate(R.layout.image, null);
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

        ImageLoader.getInstance().displayImage(urls[position], imageView, options,

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
                        Log.e(LOG_TAG, "Failed to load photo set image " + message);
                    }

                    @Override
                    public void onLoadingComplete(Bitmap loadedImage) {
                        spinner.setVisibility(View.GONE);
                        Animation anim = AnimationUtils.loadAnimation(activity,
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
