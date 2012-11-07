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

import android.support.v4.app.Fragment;
import android.view.View;

import com.krissytosi.R;

/**
 * Includes some common functionality which is shared across all fragments.
 */
public class BaseFragment extends Fragment {

    protected void toggleLoading(boolean show, View view) {
        View baseFragment = getActivity().findViewById(R.id.base_fragment);
        if (baseFragment != null && view != null) {
            baseFragment.setVisibility(show ? View.VISIBLE : View.GONE);
            view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    protected void toggleNoNetwork(boolean show) {
        View baseFragment = getActivity().findViewById(R.id.base_fragment);
        View noNetworkMessage = getActivity().findViewById(R.id.no_network_message);
        View noNetworkButton = getActivity().findViewById(R.id.no_network_button);
        baseFragment.setVisibility(show ? View.GONE : View.VISIBLE);
        noNetworkMessage.setVisibility(show ? View.VISIBLE : View.GONE);
        noNetworkButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
