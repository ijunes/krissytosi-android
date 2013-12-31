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

package com.krissytosi.api.store;

import com.etsy.etsyCore.EtsyRequestManager;
import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyRequests.EtsyRequest;
import com.krissytosi.utils.ApiConstants;

/**
 * Networked, etsy specific implementation of the {@link StoreApiClient}
 * interface.
 */
public class EtsyStoreApiClient implements StoreApiClient {

    /**
     * The manager which is responsible for putting API requests on the wire.
     */
    private EtsyRequestManager requestManager;

    @Override
    public EtsyResult runRequest(EtsyRequest etsyRequest) {
        if (requestManager == null) {
            requestManager = new EtsyRequestManager(ApiConstants.ETSY_API_KEY,
                    ApiConstants.ETSY_API_SECRET, ApiConstants.ETSY_CALLBACK,
                    ApiConstants.ETSY_SCOPE);
        }
        return requestManager.runRequest(etsyRequest);
    }

}
