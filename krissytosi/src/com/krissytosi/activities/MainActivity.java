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
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

import com.krissytosi.R;
import com.krissytosi.fragments.ContactFragment;
import com.krissytosi.fragments.HomeFragment;
import com.krissytosi.fragments.NewsFragment;
import com.krissytosi.fragments.PortfolioFragment;
import com.krissytosi.fragments.StoreFragment;
import com.krissytosi.utils.Constants;

import java.util.HashMap;

/**
 * Main activity in the application. Gives the user the choice to browse
 * portfolios, view news or contact.
 */
public class MainActivity extends FragmentActivity {

    private final static String CURRENT_TAB_IDENTIFIER = "com.krissytosi.activities.CURRENT_TAB_IDENTIFIER";

    TabHost mTabHost;
    TabManager mTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        initializeTabs(getApplicationContext().getResources());

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString(CURRENT_TAB_IDENTIFIER));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_TAB_IDENTIFIER, mTabHost.getCurrentTabTag());
    }

    private void initializeTabs(Resources resources) {
        mTabManager.addTab(
                mTabHost.newTabSpec(Constants.FRAGMENT_HOME_ID).setIndicator(
                        resources.getString(R.string.home)),
                HomeFragment.class, null);
        mTabManager.addTab(
                mTabHost.newTabSpec(Constants.FRAGMENT_PORTFOLIO_ID).setIndicator(
                        resources.getString(R.string.portfolio)),
                PortfolioFragment.class, null);
        mTabManager.addTab(
                mTabHost.newTabSpec(Constants.FRAGMENT_STORE_ID).setIndicator(
                        resources.getString(R.string.store)),
                StoreFragment.class, null);
        mTabManager.addTab(
                mTabHost.newTabSpec(Constants.FRAGMENT_NEWS_ID).setIndicator(
                        resources.getString(R.string.news)),
                NewsFragment.class, null);
        mTabManager.addTab(
                mTabHost.newTabSpec(Constants.FRAGMENT_CONTACT_ID).setIndicator(
                        resources.getString(R.string.contact)),
                ContactFragment.class, null);
    }

    public static class TabManager implements TabHost.OnTabChangeListener {

        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
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
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();
            TabInfo info = new TabInfo(tag, clss, args);
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }
            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null && mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }
                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
    }
}
