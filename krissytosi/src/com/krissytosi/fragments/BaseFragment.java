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
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.krissytosi.R;
import com.krissytosi.utils.Constants;

/**
 * Includes some common functionality which is shared across all fragments.
 */
public class BaseFragment extends Fragment implements OnClickListener {

    /**
     * Button which allows the user to re-initiate a request should they get the
     * 'No Network' message
     */
    private Button noNetworkButton;

    /**
     * Used to understand which fragment is currently selected in the tab
     * container.
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.KT_TAB_SELECTED)) {
                String tabIdentifier = intent.getStringExtra(Constants.KT_TAB_SELECTED_KEY);
                if (getFragmentIdentifier().equals(tabIdentifier)) {
                    onTabSelected();
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (getView() != null) {
            noNetworkButton = (Button) getView().findViewById(R.id.no_network_button);
            noNetworkButton.setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            IntentFilter filter = new IntentFilter(Constants.KT_TAB_SELECTED);
            getActivity().registerReceiver(broadcastReceiver, filter);
        }
        onTabSelected();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        // just to be sure
        if (view.equals(noNetworkButton)) {
            toggleNoNetwork(false, null);
            toggleLoading(true, null);
            onTabSelected();
        }
    }

    /**
     * Hide/shows a loading message.
     * 
     * @param show dictating whether the loading message should be shown.
     * @param view the view to toggle out of view if the loading view needs to
     *            be shown.
     */
    protected void toggleLoading(boolean show, View view) {
        if (getActivity() != null) {
            View baseFragment = getActivity().findViewById(R.id.base_fragment);
            if (baseFragment != null && view != null) {
                baseFragment.setVisibility(show ? View.VISIBLE : View.GONE);
                view.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
    }

    /**
     * Hides/shows a 'No Network' message.
     * 
     * @param show dictates whether the 'No Network' message should be shown.
     * @param view the view to toggle out of view if the 'No Network' view needs
     *            to be shown.
     */
    protected void toggleNoNetwork(boolean show, View view) {
        if (getActivity() != null) {
            View baseFragment = getActivity().findViewById(R.id.base_fragment);
            View loadingMessage = getActivity().findViewById(R.id.loading_message);
            View noNetworkMessage = getActivity().findViewById(R.id.no_network_message);
            View noNetworkButton = getActivity().findViewById(R.id.no_network_button);
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
     * Uniquely identifies this fragment. Any child fragment should override
     * this method and provide it's own distinct implementation.
     * 
     * @return String representation of an id.
     */
    protected String getFragmentIdentifier() {
        return "";
    }

    /**
     * Executed when a tab is selected.
     */
    protected void onTabSelected() {

    }
}
