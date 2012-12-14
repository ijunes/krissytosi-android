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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.krissytosi.KrissyTosiApplication;
import com.krissytosi.R;
import com.krissytosi.fragments.BlogFragment;
import com.krissytosi.fragments.ContactFragment;
import com.krissytosi.fragments.PhotoSetsFragment;
import com.krissytosi.fragments.StoreFragment;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.KrissyTosiUtils;
import com.krissytosi.utils.ReClickableTabHost;
import com.krissytosi.utils.ReClickableTabHostListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Main activity in the application. Gives the user the choice to browse photo
 * sets, view the etsy store, the blog or contact.
 */
public class MainActivity extends SherlockFragmentActivity {

    private static final String MAIN_ACTIVITY_STATE = "com.krissytosi.activities.MainActivity.MAIN_ACTIVITY_STATE";

    private ReClickableTabHost tabHost;
    private TabManager tabManager;

    // State
    private String fragmentIdentifierInDetailView;

    /**
     * Used to understand which fragment is currently selected in the tab
     * container.
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW_KEY.equalsIgnoreCase(intent
                    .getAction())) {
                String fragmentIdentifier = intent
                        .getStringExtra(KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW);
                if (fragmentIdentifier != null) {
                    fragmentIdentifierInDetailView = fragmentIdentifier;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        initializeViewElements();
        initializeTabs(getApplicationContext().getResources());

        if (savedInstanceState != null) {
            MainActivityState state = savedInstanceState.getParcelable(MAIN_ACTIVITY_STATE);
            tabHost.setCurrentTabByTag(state.getCurrentTabIdentifier());
            fragmentIdentifierInDetailView = state.getFragmentIdentifierInDetailView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MainActivityState state = new MainActivityState();
        state.setCurrentTabIdentifier(tabHost.getCurrentTabTag());
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
            if (!"".equalsIgnoreCase(fragmentIdentifierInDetailView)) {
                Intent intent = new Intent(KrissyTosiConstants.KT_NOTIFY_DETAIL_VIEW_KEY);
                intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IDENTIFIER_KEY,
                        fragmentIdentifierInDetailView);
                sendBroadcast(intent);
                fragmentIdentifierInDetailView = "";
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initializeViewElements() {
        tabHost = (ReClickableTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
        tabHost.setReClickableTabHostListener(tabManager);
    }

    private void initializeTabs(Resources resources) {
        tabManager.addTab(
                tabHost.newTabSpec(KrissyTosiConstants.FRAGMENT_PHOTOSETS_ID).setIndicator(
                        resources.getString(R.string.photosets)),
                PhotoSetsFragment.class, null);
        tabManager.addTab(
                tabHost.newTabSpec(KrissyTosiConstants.FRAGMENT_STORE_ID).setIndicator(
                        resources.getString(R.string.store)),
                StoreFragment.class, null);
        tabManager.addTab(
                tabHost.newTabSpec(KrissyTosiConstants.FRAGMENT_BLOG_ID).setIndicator(
                        resources.getString(R.string.blog)),
                BlogFragment.class, null);
        tabManager.addTab(
                tabHost.newTabSpec(KrissyTosiConstants.FRAGMENT_CONTACT_ID).setIndicator(
                        resources.getString(R.string.contact)),
                ContactFragment.class, null);
    }

    // Getters

    public TabHost getTabHost() {
        return tabHost;
    }

    public TabManager getTabManager() {
        return tabManager;
    }

    public String getFragmentIdentifierInDetailView() {
        return fragmentIdentifierInDetailView;
    }

    public void setFragmentIdentifierInDetailView(String fragmentIdentifierInDetailView) {
        this.fragmentIdentifierInDetailView = fragmentIdentifierInDetailView;
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    // Tab Manager Class

    public static class TabManager implements TabHost.OnTabChangeListener,
            ReClickableTabHostListener {

        private final FragmentActivity activity;
        private final TabHost tabHost;
        private final int containerId;
        private final Map<String, TabInfo> tabs = new HashMap<String, TabInfo>();
        TabInfo lastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String tag, Class<?> clazz, Bundle args) {
                this.tag = tag;
                this.clss = clazz;
                this.args = args;
            }
        }

        static class TabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public TabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            this.activity = activity;
            this.tabHost = tabHost;
            this.containerId = containerId;
            this.tabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new TabFactory(activity));
            String tag = tabSpec.getTag();
            TabInfo info = new TabInfo(tag, clss, args);
            info.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }
            tabs.put(tag, info);
            tabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = tabs.get(tabId);
            if (lastTab != newTab) {
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                if (lastTab != null && lastTab.fragment != null) {
                    ft.detach(lastTab.fragment);
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(activity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(containerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }
                lastTab = newTab;
                ft.commit();
                activity.getSupportFragmentManager().executePendingTransactions();
                Intent intent = new Intent(KrissyTosiConstants.KT_TAB_SELECTED);
                intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IDENTIFIER_KEY, newTab.tag);
                activity.sendBroadcast(intent);
                ((KrissyTosiApplication) activity.getApplication()).getTracking().trackTabChange(
                        newTab.tag);
            }
        }

        @Override
        public void onCurrentTabClicked(String tabId) {
            Intent intent = new Intent(KrissyTosiConstants.KT_CURRENT_TAB_SELECTED);
            intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IDENTIFIER_KEY, tabId);
            activity.sendBroadcast(intent);
        }
    }
}
