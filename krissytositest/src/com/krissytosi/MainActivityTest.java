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

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.krissytosi.activities.MainActivity;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.store.StoreApiClient;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.KrissyTosiTestModule;

import dagger.ObjectGraph;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        KrissyTosiApplication application = (KrissyTosiApplication) getActivity().getApplication();
        ObjectGraph objectGraph = ObjectGraph.create(new KrissyTosiTestModule());
        ApiClient apiClient = objectGraph.get(ApiClient.class);
        apiClient.setBaseUrl(ApiConstants.TEST_API_URL);
        StoreApiClient storeApiClient = objectGraph.get(StoreApiClient.class);
        Tracking tracking = objectGraph.get(Tracking.class);
        tracking.initialize(getActivity(), KrissyTosiConstants.TRACKING_KEY);
        application.setApiClient(apiClient);
        application.setStoreApiClient(storeApiClient);
        application.setTracking(tracking);
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

    @UiThreadTest
    public void testStatePause() throws Exception {
        String fragmentIdentifier = "fragmentIdentifier";
        MainActivity activity = getActivity();
        Instrumentation instr = getInstrumentation();
        activity.setFragmentIdentifierInDetailView(fragmentIdentifier);
        instr.callActivityOnPause(activity);
        instr.callActivityOnResume(activity);
        assertTrue(fragmentIdentifier.equalsIgnoreCase(activity
                .getFragmentIdentifierInDetailView()));
    }

    @UiThreadTest
    public void testStoreFragment() throws Exception {
        MainActivity activity = getActivity();
        activity.getTabHost().setCurrentTab(0);
        activity.getTabHost().setCurrentTab(1);
    }
}
