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

import com.krissytosi.api.ApiClient;
import com.krissytosi.api.NetworkedApiClient;
import com.krissytosi.api.store.EtsyStoreApiClient;
import com.krissytosi.api.store.StoreApiClient;
import com.krissytosi.tracking.Tracking;
import com.krissytosi.tracking.ga.GoogleAnalyticsTracking;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(entryPoints = {
        ApiClient.class, StoreApiClient.class, Tracking.class
})
public class KrissyTosiModule {

    @Provides
    @Singleton
    ApiClient provideApiClient() {
        return new NetworkedApiClient();
    }

    @Provides
    @Singleton
    StoreApiClient provideStoreApiClient() {
        return new EtsyStoreApiClient();
    }

    @Provides
    @Singleton
    Tracking provideTracker() {
        return new GoogleAnalyticsTracking();
    }
}
