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

package com.krissytosi.api;

import com.krissytosi.api.services.PhotoSetService;
import com.krissytosi.api.services.http.PhotoSetServiceImpl;

/**
 * Uses a HttpClient to interact with the API server.
 */
public class NetworkedApiClient implements ApiClient {

    /**
     * Member variable for the photo set service.
     */
    private PhotoSetService photoSetService;

    /**
     * Base url to target in this API client.
     */
    private String baseUrl;

    @Override
    public PhotoSetService getPhotoSetService() {
        // lazy instantiation is lazy
        if (photoSetService == null) {
            photoSetService = new PhotoSetServiceImpl();
            photoSetService.setBaseUrl(baseUrl);
        }
        return photoSetService;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
