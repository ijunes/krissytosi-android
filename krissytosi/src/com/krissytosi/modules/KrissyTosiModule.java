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

package com.krissytosi.modules;

import com.etsy.etsyCore.EtsyRequestManager;
import com.etsy.etsyCore.RequestManager;
import com.krissytosi.api.ApiClient;
import com.krissytosi.api.NetworkedApiClient;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.tracking.ga.GoogleAnalyticsTracking;
import com.krissytosi.utils.ApiConstants;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(entryPoints = {
        ApiClient.class, Tracking.class, RequestManager.class
})
public class KrissyTosiModule {

    @Provides
    @Singleton
    ApiClient provideApiClient() {
        return new NetworkedApiClient();
    }

    @Provides
    @Singleton
    Tracking provideTracker() {
        return new GoogleAnalyticsTracking();
    }

    @Provides
    @Singleton
    RequestManager provideRequestManager() {
        return new EtsyRequestManager(ApiConstants.ETSY_API_KEY,
                ApiConstants.ETSY_API_SECRET, ApiConstants.ETSY_CALLBACK,
                ApiConstants.ETSY_SCOPE);
    }
}
