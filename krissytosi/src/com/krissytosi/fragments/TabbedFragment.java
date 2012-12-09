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

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Interface defining additional methods which must be implemented by any
 * fragment in the application.
 */
public interface TabbedFragment extends OnClickListener {

    /**
     * Callback executed when a tab is selected.
     */
    void onTabSelected();

    /**
     * Callback executed when the user clicks on the currently selected tab.
     */
    void onCurrentTabClicked();

    /**
     * Uniquely identifies this fragment. Any child fragment should override
     * this method and provide it's own distinct implementation.
     * 
     * @return String representation of an id.
     */
    String getFragmentIdentifier();

    /**
     * Hides/shows a loading message.
     * 
     * @param show indicates that the loading message should be shown.
     * @param view the view to hide while the loading message is being shown.
     */
    void toggleLoading(boolean show, View view);

    /**
     * Hides/shows a 'No Network' message.
     * 
     * @param show indicates that the 'No Network' message should be shown.
     * @param view the view to hide while the 'No Network' message is being
     *            shown.
     */
    void toggleNoNetwork(boolean show, View view);

    /**
     * Gets the 'No Network' button which is associated with each fragment.
     * 
     * @return
     */
    Button getNoNetworkButton();

    /**
     * Setter for a 'No Network' button which is common across all fragments.
     * 
     * @param noNetworkButton
     */
    void setNoNetworkButton(Button noNetworkButton);
}
