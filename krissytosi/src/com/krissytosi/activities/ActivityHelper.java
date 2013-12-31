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

package com.krissytosi.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.utils.KrissyTosiConstants;

/**
 * Used to share common functionality between the two settings implementations.
 */
public class ActivityHelper {

    /**
     * Executed when a setting is altered by the user.
     * 
     * @param activity the activity which listed for this preference.
     * @param sharedPreferences the new shared preferences.
     * @param key identifying the preference which was just changed.
     */
    public static void onSharedPreferenceChanged(Activity activity,
            SharedPreferences sharedPreferences, String key) {
        if (activity != null) {
            if (key.equalsIgnoreCase(KrissyTosiConstants.TRACKING_PREFERENCE)) {
                boolean trackingEnabled = sharedPreferences.getBoolean(
                        KrissyTosiConstants.TRACKING_PREFERENCE,
                        KrissyTosiConstants.TRACKING_PREFERENCE_DEFAULT);
                Tracking tracking = ((KrissyTosiApplication) activity.getApplication())
                        .getTracking();
                if (trackingEnabled) {
                    tracking.enableTracking(activity);
                } else {
                    tracking.disableTracking(activity);
                }
            }
        }
    }

    /**
     * Used to listen for shared preferences changes. Registers/unregisters
     * listeners.
     * 
     * @param activity the activity to register. This activity <b>MUST</b>
     *            implement {@link OnSharedPreferenceChangeListener}
     * @param register boolean indicating that the activity should be
     *            registered.
     */
    public static void toggleRegisterOnSharedPreferenceChangeListener(
            Activity activity, boolean register) {
        if (register) {
            PreferenceManager.getDefaultSharedPreferences(activity)
                    .registerOnSharedPreferenceChangeListener(
                            (OnSharedPreferenceChangeListener) activity);
        } else {
            PreferenceManager.getDefaultSharedPreferences(activity)
                    .unregisterOnSharedPreferenceChangeListener(
                            (OnSharedPreferenceChangeListener) activity);
        }
    }
}
