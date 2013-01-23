/*
   Copyright 2012 - 2013 Sean O' Shea

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

package com.krissytosi.api.store;

import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyRequests.EtsyRequest;

/**
 * Hmmmm. Bleeding Etsy particulars into an interface? Suppose this is better
 * than a bunch of casting?
 */
public interface StoreApiClient {

    /**
     * Runs an Etsy API request.
     * 
     * @param etsyRequest The EtsyRequest object to run.
     * @return The Result object with the parsed HTTP response.
     */
    EtsyResult runRequest(EtsyRequest etsyRequest);

}
