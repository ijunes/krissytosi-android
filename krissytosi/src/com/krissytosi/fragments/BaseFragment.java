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

package com.krissytosi.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.krissytosi.activities.MainActivity;
import com.krissytosi.utils.KrissyTosiConstants;

/**
 * Includes some common functionality which is shared across all regular
 * fragments.
 */
public abstract class BaseFragment extends Fragment implements TabbedFragment {

    private static final String LOG_TAG = "BaseFragment";

    /**
     * Button which allows the user to re-initiate a request should they get the
     * 'No Network' message
     */
    private Button noNetworkButton;

    int flipperId;

    /**
     * Used to understand which fragment is currently selected in the tab
     * container.
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (FragmentHelper.onReceive(context, intent, getFragmentIdentifier())) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(KrissyTosiConstants.KT_TAB_SELECTED)) {
                    onTabSelected();
                } else if (action.equals(KrissyTosiConstants.KT_CURRENT_TAB_SELECTED)) {
                    onCurrentTabClicked();
                } else if (action.equals(KrissyTosiConstants.KT_NOTIFY_DETAIL_VIEW_KEY)) {
                    FragmentHelper.toggleFlipper(false,
                            (ViewFlipper) getView().findViewById(flipperId), (Activity) context,
                            getFragmentIdentifier());
                } else if (action.equalsIgnoreCase(KrissyTosiConstants.KT_PHOTOSET_LONG_PRESS)) {
                    onLongPressDetected();
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        FragmentHelper.onStart(this, getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentHelper.onResume(this, getActivity(), broadcastReceiver);
    }

    @Override
    public void onStop() {
        FragmentHelper.onStop(getActivity(), broadcastReceiver);
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        // just to be sure
        if (view.equals(noNetworkButton)) {
            FragmentHelper.handleNoNetworkButtonClick(this, getView());
        }
    }

    @Override
    public void toggleLoading(boolean show, View view) {
        FragmentHelper.toggleLoading(show, view, getView());
    }

    @Override
    public void toggleNoNetwork(boolean show, View view) {
        FragmentHelper.toggleNoNetwork(show, view, getView());
    }

    /**
     * Notifies the {@link MainActivity} that a fragment is in it's detail view.
     */
    public void toggleDetailViewNotification(boolean inDetailView) {
        if (getActivity() != null) {
            FragmentHelper.toggleDetailViewNotification(getActivity(), getFragmentIdentifier(),
                    inDetailView);
        }
    }

    @Override
    public void onCurrentTabClicked() {
        Log.d(LOG_TAG, "Current tab clicked by user");
    }

    @Override
    public void beforeDetatched() {
        Log.d(LOG_TAG, "beforeDetatched " + getFragmentIdentifier());
    }

    @Override
    public void onLongPressDetected() {
        Log.d(LOG_TAG, "onLongPressDetected");
    }

    // Getters/Setters

    @Override
    public Button getNoNetworkButton() {
        return noNetworkButton;
    }

    @Override
    public void setNoNetworkButton(Button noNetworkButton) {
        this.noNetworkButton = noNetworkButton;
    }
}
