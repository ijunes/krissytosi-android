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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.krissytosi.R;
import com.krissytosi.fragments.BlogFragment;
import com.krissytosi.fragments.ContactFragment;
import com.krissytosi.fragments.PhotoSetsFragment;
import com.krissytosi.fragments.StoreFragment;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.KrissyTosiUtils;

/**
 * Main activity in the application. Gives the user the choice to browse photo
 * sets, view the etsy store, the blog or contact.
 */
public class MainActivity extends SherlockFragmentActivity {

    private static final String MAIN_ACTIVITY_STATE = "com.krissytosi.activities.MainActivity.MAIN_ACTIVITY_STATE";

    // State
    private int fragmentIdentifierInDetailView;

    /**
     * Used to understand which fragment is currently selected in the tab
     * container.
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW_KEY.equalsIgnoreCase(intent
                    .getAction())) {
                int fragmentIdentifier = intent.getIntExtra(
                        KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW, -1);
                if (fragmentIdentifier != -1) {
                    fragmentIdentifierInDetailView = fragmentIdentifier;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        initializeTabs(getApplicationContext().getResources());

        if (savedInstanceState != null) {
            MainActivityState state = savedInstanceState.getParcelable(MAIN_ACTIVITY_STATE);
            // TODO
            // tabHost.setCurrentTabByTag(state.getCurrentTabIdentifier());
            fragmentIdentifierInDetailView = state.getFragmentIdentifierInDetailView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MainActivityState state = new MainActivityState();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            state.setCurrentTabPosition(actionBar.getSelectedTab().getPosition());
        }
        state.setFragmentIdentifierInDetailView(fragmentIdentifierInDetailView);
        outState.putParcelable(MAIN_ACTIVITY_STATE, state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW_KEY);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.xml.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            Intent intent = null;
            if (KrissyTosiUtils.atLeastHoneyComb()) {
                intent = new Intent(this, SettingsActivityHC.class);
            } else {
                intent = new Intent(this, SettingsActivity.class);
            }
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragmentIdentifierInDetailView != -1) {
                Intent intent = new Intent(KrissyTosiConstants.KT_NOTIFY_DETAIL_VIEW_KEY);
                intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IDENTIFIER_KEY,
                        fragmentIdentifierInDetailView);
                sendBroadcast(intent);
                fragmentIdentifierInDetailView = -1;
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initializeTabs(Resources resources) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar
                .addTab(actionBar
                        .newTab()
                        .setText(resources.getString(R.string.photosets))
                        .setTabListener(
                                new TabListener<PhotoSetsFragment>(this,
                                        KrissyTosiConstants.FRAGMENT_PHOTOSETS_ID,
                                        PhotoSetsFragment.class)));
        actionBar
                .addTab(actionBar
                        .newTab()
                        .setText(resources.getString(R.string.store))
                        .setTabListener(
                                new TabListener<StoreFragment>(this,
                                        KrissyTosiConstants.FRAGMENT_STORE_ID,
                                        StoreFragment.class)));
        actionBar
                .addTab(actionBar
                        .newTab()
                        .setText(resources.getString(R.string.blog))
                        .setTabListener(
                                new TabListener<BlogFragment>(this,
                                        KrissyTosiConstants.FRAGMENT_BLOG_ID,
                                        BlogFragment.class)));
        actionBar
                .addTab(actionBar
                        .newTab()
                        .setText(resources.getString(R.string.contact))
                        .setTabListener(
                                new TabListener<ContactFragment>(this,
                                        KrissyTosiConstants.FRAGMENT_CONTACT_ID,
                                        ContactFragment.class)));
    }

    // Getters/Setters

    public int getFragmentIdentifierInDetailView() {
        return fragmentIdentifierInDetailView;
    }

    public void setFragmentIdentifierInDetailView(int fragmentIdentifierInDetailView) {
        this.fragmentIdentifierInDetailView = fragmentIdentifierInDetailView;
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    // TabListener Class

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {

        private Fragment fragment;
        private final SherlockFragmentActivity activity;
        private final String tag;
        private final Class<T> clazz;

        public TabListener(SherlockFragmentActivity activity, String tag, Class<T> clz) {
            this.activity = activity;
            this.tag = tag;
            this.clazz = clz;
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (fragment == null) {
                fragment = Fragment.instantiate(activity, clazz.getName());
                ft.add(android.R.id.content, fragment, tag);
            } else {
                ft.attach(fragment);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (fragment != null) {
                ft.detach(fragment);
            }
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Intent intent = new Intent(KrissyTosiConstants.KT_CURRENT_TAB_SELECTED);
            intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IDENTIFIER_KEY, tab.getPosition());
            activity.sendBroadcast(intent);
        }
    }
}
