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

package com.krissytosi.api;

import com.krissytosi.api.services.PhotoService;
import com.krissytosi.api.services.http.PhotoServiceImpl;

/**
 * Uses a HttpClient to interact with the API server.
 */
public class NetworkedApiClient implements ApiClient {

    /**
     * Member variable for the photo set service.
     */
    private PhotoService photoService;

    /**
     * Base url to target in this API client.
     */
    private String baseUrl;

    @Override
    public PhotoService getPhotoService() {
        // lazy instantiation is lazy
        if (photoService == null) {
            photoService = new PhotoServiceImpl();
            photoService.setBaseUrl(baseUrl);
        }
        return photoService;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
