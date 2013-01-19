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

package com.krissytosi.utils;

import android.util.Log;

import com.etsy.etsyCore.EtsyResult;
import com.etsy.etsyModels.BaseModel;
import com.etsy.etsyModels.Listing;
import com.etsy.etsyRequests.EtsyRequest;
import com.krissytosi.api.store.StoreApiClient;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSystemStoreApiClient implements StoreApiClient {

    private static final String LOG_TAG = "FileSystemStoreApiClient";

    @Override
    public EtsyResult runRequest(EtsyRequest etsyRequest) {
        EtsyResult response = new EtsyResult();
        if (etsyRequest.getMethod().indexOf("shops") != -1) {
            response.setCode(HttpStatus.SC_OK);
            List<BaseModel> results = new ArrayList<BaseModel>();
            results.add(generateListing());
            response.setResults(results);
        }
        return response;
    }

    private Listing generateListing() {
        Listing listing = new Listing();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("url", "http://www.google.com");
        properties.put("title", "Title");
        properties.put("description", "Description");
        properties.put("currency_code", "USD");
        properties.put("price", "12");
        properties.put("quantity", "1");
        properties.put("when_made", "20s");
        try {
            listing.parseData(new JSONObject(properties));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "generateListing", e);
        }
        return listing;
    }
}
