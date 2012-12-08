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

package com.krissytosi;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.krissytosi.activities.MainActivity;
import com.krissytosi.utils.KrissyTosiConstants;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testSanityChecks() throws Exception {
        assertNotNull("activity should be launched successfully", getActivity());
        MainActivity activity = getActivity();
        assertNotNull("tab host should not be null", activity.getTabHost());
        assertNotNull("tab manager should not be null", activity.getTabManager());
        assertNotNull("fragment manager should not be null", activity.getSupportFragmentManager());
    }

    public void testBroadcastReceiver() throws Exception {
        Intent intent = new Intent(KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW_KEY);
        intent.putExtra(KrissyTosiConstants.KT_FRAGMENT_IN_DETAIL_VIEW, "key");
        getActivity().getBroadcastReceiver().onReceive(getActivity(), intent);
        String fragmentIdentifier = getActivity().getFragmentIdentifierInDetailView();
        assertNotNull("fragmentIdentifier should not be null", fragmentIdentifier);
        assertTrue("key".equalsIgnoreCase(fragmentIdentifier));
    }
}
