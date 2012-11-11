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

package com.krissytosi.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.krissytosi.R;

/**
 * Allows users to enable/disable a few different settings in the application.
 * This is used when the application is running < honeycomb.
 */
public class SettingsActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityHelper.toggleRegisterOnSharedPreferenceChangeListener(this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityHelper.toggleRegisterOnSharedPreferenceChangeListener(this, false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ActivityHelper.onSharedPreferenceChanged(this, sharedPreferences, key);
    }
}
