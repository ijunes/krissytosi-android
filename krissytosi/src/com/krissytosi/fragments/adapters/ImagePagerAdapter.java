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
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.krissytosi.R;
import com.krissytosi.utils.KrissyTosiConstants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Used to show a list of swipe-able images.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private static final String LOG_TAG = "ImagePagerAdapter";

    private final LayoutInflater inflater;
    private final String[] urls;
    private final DisplayImageOptions options;
    private final Animation animation;
    private final Context context;
    private final int tabPosition;
    private boolean pressed;
    private final Handler handle = new Handler();
    private final Runnable longClick = new Runnable() {
        @Override
        public void run() {
            if (pressed) {
                Intent intent = new Intent(KrissyTosiConstants.KT_PHOTOSET_LONG_PRESS);
                intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IDENTIFIER_KEY, tabPosition);
                context.sendBroadcast(intent);
            }
        }
    };

    public ImagePagerAdapter(String[] urls, LayoutInflater layoutInflater,
            Animation fadeInAnimation, Context intentContext, int tabPosition) {
        this.urls = urls;
        this.options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        inflater = layoutInflater;
        animation = fadeInAnimation;
        context = intentContext;
        this.tabPosition = tabPosition;
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
        final View imageLayout = inflater.inflate(R.layout.image, null);
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);

        imageView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (attacher.onTouch(v, event)) {
                    final int action = event.getAction();
                    switch (action & MotionEventCompat.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            pressed = true;
                            handle.postDelayed(longClick, 800);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            pressed = false;
                            handle.removeCallbacks(longClick);
                            break;
                        default:
                            break;
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

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
                                ImageLoader.getInstance().clearMemoryCache();
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
                        imageView.setAnimation(animation);
                        animation.start();
                        Intent intent = new Intent(KrissyTosiConstants.KT_PHOTO_LOADED);
                        intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IDENTIFIER_KEY,
                                KrissyTosiConstants.FRAGMENT_STORE_POSITION);
                        intent.putExtra(KrissyTosiConstants.KT_PHOTO_LOADED_HEIGHT,
                                loadedImage.getHeight());
                        intent.putExtra(KrissyTosiConstants.KT_PHOTO_LOADED_WIDTH,
                                loadedImage.getWidth());
                        context.sendBroadcast(intent);
                        attacher.update();
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
