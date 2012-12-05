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

import static com.google.code.tempusfugit.temporal.Duration.millis;
import static com.google.code.tempusfugit.temporal.Timeout.timeout;

import android.test.AndroidTestCase;
import android.util.Log;

import com.google.code.tempusfugit.temporal.Condition;
import com.google.code.tempusfugit.temporal.WaitFor;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.NetworkedApiClient;
import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.utils.ApiConstants;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class NetworkTest extends AndroidTestCase {

    private static final String LOG_TAG = "NetworkTest";

    private List<PhotoSet> photoSets;

    public void testGetPhotoSets() {
        try {
            ApiClient apiClient = new NetworkedApiClient();
            apiClient.setBaseUrl(ApiConstants.PROD_API_URL);
            photoSets = apiClient.getPhotoSetService().getPhotoSets();
            WaitFor.waitOrTimeout(new PhotoSetsConditionCheck(),
                    timeout(millis(10000)));
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "testGetPhotoSets", e);
        } catch (TimeoutException e) {
            fail();
            Log.e(LOG_TAG, "testGetPhotoSets", e);
        }
    }

    private class PhotoSetsConditionCheck implements Condition {

        public PhotoSetsConditionCheck() {

        }

        @Override
        public boolean isSatisfied() {
            return photoSets != null && photoSets.size() != 0;
        }
    }
}
