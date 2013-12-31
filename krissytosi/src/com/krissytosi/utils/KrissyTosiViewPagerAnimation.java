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

package com.krissytosi.utils;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Custom animation used to resize {@link ViewPager} views when displaying
 * images of varying sizes.
 */
public class KrissyTosiViewPagerAnimation extends Animation {

    private final int startingHeight;
    private final int targetHeight;
    private final View view;

    public KrissyTosiViewPagerAnimation(View view, int targetHeight, int startingHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.startingHeight = startingHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        newHeight = (int) ((targetHeight - startingHeight) * interpolatedTime) + startingHeight;
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
            int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
