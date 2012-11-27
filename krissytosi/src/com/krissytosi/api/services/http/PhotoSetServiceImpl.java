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

package com.krissytosi.api.services.http;

import com.krissytosi.api.domain.PhotoSet;
import com.krissytosi.api.modules.ApiModule;
import com.krissytosi.api.parse.PhotoSetParser;
import com.krissytosi.api.services.PhotoSetService;
import com.krissytosi.utils.ApiConstants;

import dagger.ObjectGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP specific implementation of a {@link PhotoSetService}.
 */
public class PhotoSetServiceImpl extends HttpService implements
        PhotoSetService {

    private PhotoSetParser parser;

    /**
     * The base url which this service is targeting.
     */
    private String baseUrl;

    @Override
    public List<PhotoSet> getPhotoSets() {
        // prepare the options.
        Map<String, String> options = new HashMap<String, String>();
        options.put(ApiConstants.FLICKR_USER_ID_PARAM, ApiConstants.FLICKR_USER_ID);
        // prepare the url
        String photoSetUrl = createUrl(baseUrl, options);
        // execute the request
        String response = doGet(photoSetUrl);
        // parse & return the response
        return getPhotoSetParser().parsePhotoSets(response);
    }

    // Getters/Setters

    private PhotoSetParser getPhotoSetParser() {
        if (parser == null) {
            ObjectGraph objectGraph = ObjectGraph.create(new ApiModule());
            parser = objectGraph.get(PhotoSetParser.class);
        }
        return parser;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
