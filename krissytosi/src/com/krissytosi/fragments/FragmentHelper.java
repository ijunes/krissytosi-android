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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.krissytosi.R;
import com.krissytosi.fragments.views.BaseDetailView;
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
     */
    public static void onStart(TabbedFragment fragment, View baseView) {
        if (baseView != null) {
            View button = baseView.findViewById(R.id.no_network_button);
            if (button != null) {
                fragment.setNoNetworkButton((Button) button);
                fragment.getNoNetworkButton().setOnClickListener(fragment);
            }
        }
    }

    /**
     * Common lifecycle functionality for a Fragment's onResume method.
     * 
     * @param fragment the fragment which was just resumed.
     * @param activity the activity associated with the fragment.
     * @param broadcastReceiver common broadcast receiver which needs to be
     *            registered to the activity when the fragment is resumed.
     */
    public static void onResume(TabbedFragment fragment, FragmentActivity activity,
            BroadcastReceiver broadcastReceiver) {
        if (activity != null) {
            IntentFilter filter = new IntentFilter(KrissyTosiConstants.KT_TAB_SELECTED);
            filter.addAction(KrissyTosiConstants.KT_CURRENT_TAB_SELECTED);
            filter.addAction(KrissyTosiConstants.KT_NOTIFY_DETAIL_VIEW_KEY);
            filter.addAction(KrissyTosiConstants.KT_PHOTO_LOADED);
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
     * Executed when a fragment receives a broadcast.
     * 
     * @param context the context in which the broadcast was received.
     * @param intent the intent which was broadcast.
     * @param fragmentIdentifier identifying this fragment.
     * @return boolean indicating that this broadcast was intended for *this*
     *         fragment.
     */
    public static boolean onReceive(Context context, Intent intent, int fragmentIdentifier) {
        boolean isIntendedForThisFragment = false;
        String action = intent.getAction();
        if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
                && fragmentIdentifier == intent.getIntExtra(
                        KrissyTosiConstants.KT_FRAGMENT_IDENTIFIER_KEY, -1)) {
            isIntendedForThisFragment = true;
        }
        return isIntendedForThisFragment;
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
            View progressBar = baseView.findViewById(R.id.loading_progress_bar);
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
                progressBar.setVisibility(View.GONE);
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
            if (baseFragment != null) {
                View loadingMessage = baseView.findViewById(R.id.loading_message);
                View loadingProgressBar = baseView.findViewById(R.id.loading_progress_bar);
                loadingMessage.setVisibility(show ? View.VISIBLE : View.GONE);
                loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                baseFragment.setVisibility(show ? View.VISIBLE : View.GONE);
                if (view != null) {
                    view.setVisibility(show ? View.GONE : View.VISIBLE);
                }
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

    /**
     * Hides/shows elements in a {@link ViewFlipper} with an animation based on
     * the parameters.
     * 
     * @param show whether or not to show or hide the left most view in the
     *            {@link ViewFlipper}.
     * @param flipper the {@link ViewFlipper} to hide/show.
     * @param activity the activity associated with the {@link ViewFlipper}.
     * @param fragmentIdentifier the id for the fragment in question.
     */
    public static void toggleFlipper(boolean show, ViewFlipper flipper, Activity activity,
            int fragmentIdentifier) {
        if (flipper != null) {
            if (show) {
                flipper.setInAnimation(AnimationUtils.loadAnimation(activity,
                        android.R.anim.slide_in_left));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(activity,
                        android.R.anim.slide_out_right));
                flipper.showPrevious();
            } else {
                flipper.setInAnimation(AnimationUtils.loadAnimation(activity,
                        R.anim.slide_in_right));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(activity,
                        R.anim.slide_out_left));
                flipper.showNext();
            }
        }
        toggleDetailViewNotification(activity, fragmentIdentifier, show);
    }

    public static void toggleDetailViewNotification(Activity activity, int fragmentIdentifier,
            boolean inDetailView) {
        if (activity != null) {
            Intent intent = new Intent(KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW_KEY);
            intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW, fragmentIdentifier);
            activity.sendBroadcast(intent);
        }
    }

    /**
     * Sets the action bar title for an activity.
     * 
     * @param activity the activity in question.
     * @param title the title to set.
     */
    public static void setTitle(FragmentActivity activity, String title) {
        if (activity != null) {
            SherlockFragmentActivity sherlockFragmentActivity = (SherlockFragmentActivity) activity;
            sherlockFragmentActivity.getSupportActionBar().setTitle(Html.fromHtml(title));
        }
    }

    /**
     * @param detailView
     */
    public static void handleDetailViewBeforeDetatched(BaseDetailView detailView) {
        if (detailView != null) {
            detailView.beforeDetatched();
        }
    }
}
