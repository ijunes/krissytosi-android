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

package com.krissytosi.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.krissytosi.R;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.KrissyTosiUtils;

/**
 * Includes common functionality which is shared between the
 * {@link BaseFragment} & {@link BaseListFragment} classes. This class is
 * necessary as there are divergent hierarchies in the fragment API when it
 * comes to dealing with simple fragments and dealing with fragments which
 * contain {@link ListView}s.
 */
public class FragmentHelper {

    /**
     * Common lifecycle functionality for a Fragment's onStart method.
     * 
     * @param fragment the fragment which was just started.
     * @param baseView the view associated with the fragment.
     * @param noNetworkButton button which allows the user to reinitiate API
     *            calls should their network be initially down.
     */
    public static void onStart(TabbedFragment fragment, View baseView, Button noNetworkButton) {
        if (baseView != null) {
            View button = baseView.findViewById(R.id.no_network_button);
            if (button != null) {
                noNetworkButton = (Button) button;
                noNetworkButton.setOnClickListener(fragment);
            }
        }
    }

    /**
     * Common lifecycle functionality for a Fragment's onResume method.
     * 
     * @param fragment the fragment which was just resumned.
     * @param activity the activity associated with the fragment.
     * @param broadcastReceiver common broadcast receiver which needs to be
     *            registered to the activity when the fragment is resumed.
     */
    public static void onResume(TabbedFragment fragment, FragmentActivity activity,
            BroadcastReceiver broadcastReceiver) {
        if (activity != null) {
            IntentFilter filter = new IntentFilter(KrissyTosiConstants.KT_TAB_SELECTED);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            activity.registerReceiver(broadcastReceiver, filter);
        }
        fragment.onTabSelected();
    }

    /**
     * Common lifecycle functionality for a Fragment's onStop method.
     * 
     * @param activity activity associated with the fragment which was just
     *            stopped.
     * @param broadcastReceiver receiver which needs to be unregistered after
     *            the activity is stopped.
     */
    public static void onStop(FragmentActivity activity, BroadcastReceiver broadcastReceiver) {
        if (activity != null) {
            activity.unregisterReceiver(broadcastReceiver);
        }
    }

    /**
     * Executed when a fragment receives a broadcast indicating that a tab has
     * been selected.
     * 
     * @param context the context in which the tab was selected.
     * @param intent the intent which was broadcast.
     * @param fragmentIdentifier identifying this fragment.
     * @return boolean indicating that this broadcast was intended for *this*
     *         fragment.
     */
    public static boolean onReceive(Context context, Intent intent, String fragmentIdentifier) {
        boolean shouldSelectTab = false;
        String action = intent.getAction();
        if (action.equals(KrissyTosiConstants.KT_TAB_SELECTED)) {
            String tabIdentifier = intent.getStringExtra(KrissyTosiConstants.KT_TAB_SELECTED_KEY);
            if (fragmentIdentifier.equals(tabIdentifier)) {
                shouldSelectTab = true;
            }
        } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // TODO
        }
        return shouldSelectTab;
    }

    /**
     * Hides/shows a 'No Network' message.
     * 
     * @param show dictates whether the 'No Network' message should be shown.
     * @param view the view to toggle out of view if the 'No Network' view needs
     *            to be shown.
     * @param baseView the root view which contains the view parameter.
     */
    public static void toggleNoNetwork(boolean show, View view, View baseView) {
        if (baseView != null) {
            String noNetworkString = baseView.getResources().getString(R.string.no_network);
            // before telling the user that they should connect to the internet
            // to view this content, ensure that there isn't something amiss
            // with the API servers.
            if (KrissyTosiUtils.isNetworkAvailable(baseView.getContext())) {
                noNetworkString = baseView.getResources().getString(R.string.no_api);
            }
            View baseFragment = baseView.findViewById(R.id.base_fragment);
            View loadingMessage = baseView.findViewById(R.id.loading_message);
            TextView noNetworkMessage = (TextView) baseView.findViewById(R.id.no_network_message);
            View noNetworkButton = baseView.findViewById(R.id.no_network_button);
            noNetworkMessage.setText(noNetworkString);
            baseFragment.setVisibility(show ? View.VISIBLE : View.GONE);
            noNetworkMessage.setVisibility(show ? View.VISIBLE : View.GONE);
            noNetworkButton.setVisibility(show ? View.VISIBLE : View.GONE);
            if (view != null) {
                view.setVisibility(show ? View.GONE : View.VISIBLE);
            }
            if (show) {
                loadingMessage.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Hide/shows a loading message.
     * 
     * @param show dictating whether the loading message should be shown.
     * @param view the view to toggle out of view if the loading view needs to
     *            be shown.
     * @param baseView the root view which contains the view parameter.
     */
    public static void toggleLoading(boolean show, View view, View baseView) {
        if (baseView != null) {
            View baseFragment = baseView.findViewById(R.id.base_fragment);
            if (baseFragment != null && view != null) {
                View loadingMessage = baseView.findViewById(R.id.loading_message);
                loadingMessage.setVisibility(show ? View.VISIBLE : View.GONE);
                baseFragment.setVisibility(show ? View.VISIBLE : View.GONE);
                view.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
    }

    /**
     * Executed when the user clicks on the 'No Network' button. Re-initiates
     * the API call which usually builds the page.
     * 
     * @param fragment the fragment associated with the button.
     * @param baseView the view in which the 'No Network' button lives.
     */
    public static void handleNoNetworkButtonClick(TabbedFragment fragment, View baseView) {
        toggleNoNetwork(false, null, baseView);
        toggleLoading(true, null, baseView);
        fragment.onTabSelected();
    }
}
