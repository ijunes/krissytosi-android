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
import com.krissytosi.utils.KrissyTosiConstants;

/**
 * Defines all methods used to interact with the API server.
 */
public interface ApiClient {

    /**
     * Sets the base URL which this API client should target. See
     * {@link KrissyTosiConstants} for example base urls.
     * 
     * @param baseUrl the base url to set.
     */
    void setBaseUrl(String baseUrl);

    /**
     * Accessor for the photo service.
     * 
     * @return an instance of a {@link PhotoService}.
     */
    PhotoService getPhotoService();
}
