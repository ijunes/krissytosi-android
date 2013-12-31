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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krissytosi.R;
import com.krissytosi.utils.KrissyTosiConstants;

public class ContactFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact, container, false);
    }

    @Override
    public void onTabSelected() {
        if (getView() != null) {
            getView().findViewById(R.id.base_fragment).setVisibility(View.GONE);
        }
        FragmentHelper.setTitle(getActivity(), getResources().getString(R.string.app_name));
    }

    @Override
    public int getFragmentIdentifier() {
        return KrissyTosiConstants.FRAGMENT_CONTACT_POSITION;
    }
}
