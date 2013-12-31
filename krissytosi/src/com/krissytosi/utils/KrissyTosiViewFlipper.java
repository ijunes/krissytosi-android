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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ViewFlipper;

/**
 * Provides some try/catching logic related to the {@link ViewFlipper} class.
 * See http://code.google.com/p/android/issues/detail?id=6191 for more details.
 */
public class KrissyTosiViewFlipper extends ViewFlipper {

    private static final String LOG_TAG = "KrissyTosiViewFlipper";

    public KrissyTosiViewFlipper(Context context) {
        super(context);
    }

    public KrissyTosiViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (Exception e) {
            Log.d(LOG_TAG, "onDetatchedFromWindow", e);
            stopFlipping();
        }
    }

}
