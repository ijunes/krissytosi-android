/*
   Copyright 2012 - 2013 Sean O' Shea

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
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.krissytosi.utils.KrissyTosiConstants;

/**
 * Base class for {@link Fragment}s which need a {@link ListView}.
 */
public abstract class BaseListFragment extends ListFragment implements TabbedFragment {

    private static final String LOG_TAG = "BaseListFragment";

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
                } else if (action.equals(KrissyTosiConstants.KT_PHOTO_LOADED)) {
                    onPhotoLoaded(
                            intent.getIntExtra(KrissyTosiConstants.KT_PHOTO_LOADED_HEIGHT, 0),
                            intent.getIntExtra(KrissyTosiConstants.KT_PHOTO_LOADED_WIDTH, 0));
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
     * Callback executed when a ImagePagerAdapter loads an image.
     * 
     * @param height the height of the loaded image.
     * @param width the width of the loaded image.
     */
    public abstract void onPhotoLoaded(int height, int width);

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
