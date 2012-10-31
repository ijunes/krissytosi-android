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

package com.krissytosi.tracking;

import android.content.Context;

/**
 * Defines methods which should be implemented by any reasonable analytics
 * implementation. Any user interaction which is track-able should be part of
 * this interface.
 */
public interface Tracking {

    /**
     * Executed when the application wishes to init the tracking code.
     * 
     * @param context the app context.
     * @param key typically the API/tracking key for the app. This is usually
     *            retrieved from the third-party tracking company.
     */
    public void initialize(Context context, String key);

    /**
     * Executed when a user changes to a different tab.
     * 
     * @param tabIdentifier unique identifier for the tab.
     */
    public void trackTabChange(String tabIdentifier);

    /**
     * Executed when a user changes to a different portfolio.
     * 
     * @param portfolioIdentifier unique identifier for a portfolio.
     */
    public void trackPortfolioChange(String portfolioIdentifier);

    /**
     * Executed when a user decides that they want to share one of the images in
     * a particular portfolio.
     * 
     * @param portfolioIdentifier unique identifier for a portfolio.
     * @param mode the way in which the media was shared (Twitter, Facebook,
     *            etc)
     */
    public void mediaShared(String portfolioIdentifier, long mode);

    /**
     * Disables tracking.
     * 
     * @param context the app context.
     */
    public void disableTracking(Context context);

    /**
     * Enables tracking.
     * 
     * @param context the app context.
     */
    public void enableTracking(Context context);
}
