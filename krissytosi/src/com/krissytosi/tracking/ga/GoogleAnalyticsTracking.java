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

package com.krissytosi.tracking.ga;

import android.content.Context;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.utils.TrackingConstants;

/**
 * GA specific implementation of the {@link Tracking} interface.
 */
public class GoogleAnalyticsTracking implements Tracking {

    /**
     * Instance of the GA tracker.
     */
    private Tracker tracker;

    /**
     * Determines whether or not the tracking is enabled. Defaults to true.
     */
    private boolean enabled = true;

    private final int sampleRate = TrackingConstants.DEFAULT_SAMPLE_RATE_SECONDS;

    private final boolean debugEnabled = TrackingConstants.DEFAULT_DEBUG_ENABLED;

    @Override
    public synchronized void initialize(Context context, String key) {
        if (enabled && tracker == null) {
            GoogleAnalytics.getInstance(context).setDebug(debugEnabled);
            tracker = GoogleAnalytics.getInstance(context).getTracker(key);
            tracker.setSampleRate(sampleRate);
        }
    }

    @Override
    public void trackTabChange(String tabIdentifier) {
        if (enabled) {
            tracker.trackEvent(TrackingConstants.CATEGORY_IDENTIFIER_UI_ACTION,
                    TrackingConstants.ACTION_IDENTIFIER_TAB_PRESS,
                    tabIdentifier, (long) 0);
        }
    }

    @Override
    public void trackPhotoSetChange(String photoSetIdentifier) {
        if (enabled) {
            tracker.trackEvent(TrackingConstants.CATEGORY_IDENTIFIER_UI_ACTION,
                    TrackingConstants.ACTION_IDENTIFIER_PHOTOSET_CHANGE,
                    photoSetIdentifier, (long) 0);
        }
    }

    @Override
    public void mediaShared(String photoSetIdentifier, long mode) {
        if (enabled) {
            tracker.trackEvent(TrackingConstants.CATEGORY_IDENTIFIER_UI_ACTION,
                    TrackingConstants.ACTION_IDENTIFIER_MEDIA_SHARE,
                    photoSetIdentifier, mode);
        }
    }

    @Override
    public void disableTracking(Context context) {
        toggleEnableTracking(context, false);
    }

    @Override
    public void enableTracking(Context context) {
        toggleEnableTracking(context, true);
    }

    private void toggleEnableTracking(Context context, boolean enable) {
        enabled = enable;
        GoogleAnalytics.getInstance(context).setAppOptOut(!enable);
    }

}
