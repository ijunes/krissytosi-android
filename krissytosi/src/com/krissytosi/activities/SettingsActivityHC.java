
package com.krissytosi.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.krissytosi.fragments.SettingsFragment;

@SuppressLint("NewApi")
public class SettingsActivityHC extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
