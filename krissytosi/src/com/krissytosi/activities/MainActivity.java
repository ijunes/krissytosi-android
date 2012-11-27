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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import com.krissytosi.fragments.NewsFragment;
import com.krissytosi.fragments.PhotoSetsFragment;
import com.krissytosi.fragments.StoreFragment;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.KrissyTosiUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Main activity in the application. Gives the user the choice to browse photo
 * sets, view news or contact.
 */
public class MainActivity extends SherlockFragmentActivity {

    private static final String CURRENT_TAB_IDENTIFIER = "com.krissytosi.activities.CURRENT_TAB_IDENTIFIER";

    private TabHost tabHost;
    private TabManager tabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        initializeViewElements();
        initializeTabs(getApplicationContext().getResources());

        if (savedInstanceState != null) {
            tabHost.setCurrentTabByTag(savedInstanceState.getString(CURRENT_TAB_IDENTIFIER));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_TAB_IDENTIFIER, tabHost.getCurrentTabTag());
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

    private void initializeViewElements() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
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
                tabHost.newTabSpec(KrissyTosiConstants.FRAGMENT_NEWS_ID).setIndicator(
                        resources.getString(R.string.news)),
                NewsFragment.class, null);
        tabManager.addTab(
                tabHost.newTabSpec(KrissyTosiConstants.FRAGMENT_CONTACT_ID).setIndicator(
                        resources.getString(R.string.contact)),
                ContactFragment.class, null);
    }

    public static class TabManager implements TabHost.OnTabChangeListener {

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

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
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
            tabSpec.setContent(new DummyTabFactory(activity));
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
                intent.putExtra(KrissyTosiConstants.KT_TAB_SELECTED_KEY, newTab.tag);
                activity.sendBroadcast(intent);
                ((KrissyTosiApplication) activity.getApplication()).getTracking().trackTabChange(
                        newTab.tag);
            }
        }
    }
}
