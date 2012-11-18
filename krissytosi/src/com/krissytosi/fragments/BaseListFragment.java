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
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * Base class for {@link Fragment}s which need a {@link ListView}.
 */
public abstract class BaseListFragment extends ListFragment implements TabbedFragment {

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
            if (FragmentHelper.onReceive(context, intent, getFragmentIdentifier())) {
                onTabSelected();
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
        super.onStop();
        FragmentHelper.onStop(getActivity(), broadcastReceiver);
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
