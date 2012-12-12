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

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.krissytosi.activities.MainActivity;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.store.StoreApiClient;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.utils.ApiConstants;
import com.krissytosi.utils.KrissyTosiConstants;
import com.krissytosi.utils.KrissyTosiTestModule;
import com.krissytosi.utils.ReClickableTabHost;

import dagger.ObjectGraph;

public class IntegrationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public IntegrationTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
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

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testAFewSanityChecks() throws Exception {
        solo.waitForActivity("MainActivity");
        assertTrue(solo.getView(android.R.id.tabcontent).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(android.R.id.tabs).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(android.R.id.tabhost).getVisibility() == View.VISIBLE);
        final ReClickableTabHost tabHost = (ReClickableTabHost) solo.getView(android.R.id.tabhost);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tabHost.setCurrentTab(0);
                tabHost.setCurrentTab(1);
                tabHost.setCurrentTab(3);
            }
        });
    }
}
